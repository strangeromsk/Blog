package main.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.API.ResponseApi;
import main.DTO.StatResponse;
import main.DTO.UserMyProfileDto;
import main.DTO.UserRegisterResponse;
import main.DTO.moderation.UserModerationDto;
import main.mapper.UserMapper;
import main.model.Post;
import main.model.User;
import main.repositories.CaptchaRepository;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import main.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.toIntExact;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final CaptchaRepository captchaRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageServiceImpl fileStorageService;
    private final EmailServiceImpl emailService;

    public boolean isModerator(int id){
        return userRepository.getOne(id).getIsModerator() == 1;
    }

    @Getter
    protected Map<String, Integer> sessionIds = new ConcurrentHashMap<>();

    public UserModerationDto mapUserModeration(User user){
        return userMapper.toDtoModeration(user);
    }

    public UserServiceImpl(CaptchaRepository captchaRepository, UserMapper userMapper, UserRepository userRepository,
                           PostRepository postRepository, PasswordEncoder passwordEncoder, FileStorageServiceImpl fileStorageService, EmailServiceImpl emailService) {
        this.captchaRepository = captchaRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
    }

    public User getUser(int userId){
        return userRepository.getOne(userId);
    }

    public ResponseEntity<ResponseApi> populateUserOnLogin(String email, String password){
        ResponseApi responseApi;
        Optional<User> userOptional = userRepository.findByEmail(email);
        Map<String, String> errors = new HashMap<>(8);

        if(userOptional.isEmpty()){
            errors.put("email", "User does not exist");
            responseApi = ResponseApi.builder().errors(errors)
                    .result(false).build();
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }else if(!passwordEncoder.matches(password, userOptional.get().getPassword())) {
            errors.put("password", "Password is incorrect");
            responseApi = ResponseApi.builder().errors(errors)
                    .result(false).build();
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }else {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            sessionIds.put(sessionId, userOptional.get().getId());
            UserModerationDto userModerationDto = mapUserModeration(userOptional.get());
            if(userOptional.get().getIsModerator() == 1){
                userModerationDto.setModeration(true);
                userModerationDto.setSettings(true);
            }
            userModerationDto.setModerationCount(toIntExact(postRepository.countNewPostsToModerator()));
            responseApi = ResponseApi.builder()
                    .result(true)
                    .user(userModerationDto)
                    .build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
    }

    public ResponseApi<UserModerationDto> checkUserAuth(int id){
        ResponseApi responseApi;
        User user = userRepository.getOne(id);
        UserModerationDto userModerationDto = mapUserModeration(user);
        if (user.getIsModerator() == 1) {
            userModerationDto.setModeration(true);
            userModerationDto.setSettings(true);
        }
        userModerationDto.setModerationCount(toIntExact(postRepository.countNewPostsToModerator()));
        responseApi = ResponseApi.builder()
                .result(true)
                .user(userModerationDto)
                .build();
        return responseApi;
    }

    @Transactional
    public ResponseApi restorePassword(String email, HttpServletRequest request){
        String appUrl = request.getScheme() + "://" + request.getServerName();
        ResponseApi responseApi;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String randomHash = UUID.randomUUID().toString();
            user.setCode(randomHash);
            userRepository.save(user);
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("MyBlog@example.com");
            passwordResetEmail.setTo(email);
            passwordResetEmail.setText(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link:\n" + appUrl + "/login/change-password/" + randomHash);
            emailService.sendEmail(passwordResetEmail);

            responseApi = ResponseApi.builder()
                    .result(true).build();
        }else {
            responseApi = ResponseApi.builder()
                    .result(false).build();
        }
        return responseApi;
    }

    @Transactional
    public ResponseEntity<ResponseApi> register(UserRegisterResponse userRegisterResponse){
        String email = userRegisterResponse.getEmail();
        String name = userRegisterResponse.getName();
        String password = userRegisterResponse.getPassword();
        String captcha = userRegisterResponse.getCaptcha();
        String secretCode = userRegisterResponse.getCaptchaSecret();
        ResponseApi responseApi;
        int maxNameLength = 12;
        int minNameLength = 3;
        int minPasswordLength = 6;
        Map<String, String> errors = new HashMap<>(8);

        Optional<String> captchaServer = captchaRepository.getCaptchaBySecretCode(secretCode);
        boolean captchaExists = captchaServer.isPresent();
        if(captchaExists && !captcha.equals(captchaServer.get())){
            errors.put("captcha", "Captcha code is incorrect!");
            responseApi = ResponseApi.builder()
                    .result(false).errors(errors).build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            errors.put("email", "Email is already registered and/or incorrect");
            responseApi = ResponseApi.builder()
                        .result(false).errors(errors).build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }else {
            if(name.length() > maxNameLength || name.length() < minNameLength){
                errors.put("name", "Name is incorrect");
            }
            if(password.length() < minPasswordLength){
                errors.put("password", "Password is less than 6 symbols");
            }
            responseApi = ResponseApi.builder()
                    .result(false).errors(errors).build();
            if(errors.size() == 0){
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(passwordEncoder.encode(password));
                user.setCode(secretCode);
                user.setRegTime(new Date().getTime());
                user.setIsModerator(0);
                userRepository.save(user);
                responseApi = ResponseApi.builder()
                        .result(true).build();
                return new ResponseEntity<>(responseApi, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(responseApi, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changeMyProfile(UserMyProfileDto userMyProfileDto, User user){
        ResponseApi responseApi;
        int maxNameLength = 12;
        int minNameLength = 3;
        int minPasswordLength = 6;
        String emailRegex = "\\w{0,64}@\\w{0,60}\\.\\w{0,4}";
        Optional<String> email = Optional.ofNullable(userMyProfileDto.getEmail());
        Optional<String> name = Optional.ofNullable(userMyProfileDto.getName());
        Optional<String> password = Optional.ofNullable(userMyProfileDto.getPassword());
        boolean isEmailCorrect = email.get().matches(emailRegex);

        byte removePhoto = userMyProfileDto.getRemovePhoto();
        Map<String, String> errors = new HashMap<>(8);

        if(!isEmailCorrect){
            errors.put("email", "Email is already registered and/or incorrect");
        }
        if(name.isPresent() && (name.get().length() > maxNameLength || name.get().length() < minNameLength)){
            errors.put("name", "Name is incorrect");
        }
        if(password.isPresent() && !password.get().equals("") && password.get().length() < minPasswordLength){
            errors.put("password", "Password is less than 6 symbols");
        }
        responseApi = ResponseApi.builder()
                .result(false).errors(errors).build();
        if(errors.size() == 0){
            email.ifPresent(user::setEmail);
            name.ifPresent(user::setName);
            password.ifPresent(s -> user.setPassword(passwordEncoder.encode(s)));
            if(removePhoto == 1){
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                File photo = new File(s + "\\" + user.getPhoto());
                user.setPhoto("");
                photo.delete();
            }
            userRepository.save(user);
            responseApi = ResponseApi.builder()
                    .result(true).build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changeMyProfileWithPhoto(MultipartFile photo, String name, String email,
                                                                String password, int removePhoto, User user,
                                                                HttpServletRequest request){
        ResponseApi responseApi;
        int maxNameLength = 12;
        int minNameLength = 3;
        int minPasswordLength = 6;
        String emailRegex = "\\w{0,64}@\\w{0,60}\\.\\w{0,4}";
        Optional<String> emailOptional = Optional.ofNullable(email);
        Optional<String> nameOptional = Optional.ofNullable(name);
        Optional<String> passwordOptional = Optional.ofNullable(password);
        boolean isEmailCorrect = emailOptional.get().matches(emailRegex);

        Map<String, String> errors = new HashMap<>(8);
        if(!isEmailCorrect){
            errors.put("email", "Email is already registered and/or incorrect");
        }
        if(nameOptional.isPresent() && (nameOptional.get().length() > maxNameLength || nameOptional.get().length() < minNameLength)){
            errors.put("name", "Name is incorrect");
        }
        if(passwordOptional.isPresent() && !passwordOptional.get().equals("") && passwordOptional.get().length() < minPasswordLength){
            errors.put("password", "Password is less than 6 symbols");
        }

        responseApi = ResponseApi.builder()
                .result(false).errors(errors).build();
        if(errors.size() == 0){
            emailOptional.ifPresent(user::setEmail);
            nameOptional.ifPresent(user::setName);
            passwordOptional.ifPresent(s -> user.setPassword(passwordEncoder.encode(s)));
            user.setPhoto(fileStorageService.storeFileResized(photo, request));
            if(removePhoto == 1){
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                File photoNew = new File(s + "\\" + user.getPhoto());
                photoNew.delete();
                user.setPhoto("");
            }
            userRepository.save(user);
            responseApi = ResponseApi.builder()
                    .result(true).build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }

    public StatResponse myStatistics(int userId){
        List<Post> postsList = postRepository.findAllPostsByUserId(userId);
        return getStatResponse(postsList);
    }

    public StatResponse allStatistics(){
        List<Post> postsList = postRepository.findAll();
        return getStatResponse(postsList);
    }

    private StatResponse getStatResponse(List<Post> postsList) {
        StatResponse statResponse = new StatResponse();
        if(postsList.size() != 0){
            statResponse.setPostsCount(postsList.size());
            statResponse.setLikesCount(postsList
                    .stream()
                    .flatMap(e -> e.getPostVotes().stream().filter(k -> k.getValue() == 1))
                    .count());
            statResponse.setDislikesCount(postsList
                    .stream()
                    .flatMap(e -> e.getPostVotes().stream().filter(k -> k.getValue() == -1))
                    .count());
            statResponse.setViewsCount(postsList.stream().mapToLong(Post::getViewCount).sum());
            statResponse.setFirstPublication(postsList.stream().map(Post::getTimestamp).min(Long::compareTo).orElse(0L));
        }else {
            statResponse.setPostsCount(0);
            statResponse.setLikesCount(0);
            statResponse.setDislikesCount(0);
            statResponse.setViewsCount(0);
        }
        log.info("Statresponse postsCount:{}, likesCount:{}, dislikesCount:{}, viewsCount:{}", statResponse.getPostsCount(),
                statResponse.getLikesCount(), statResponse.getDislikesCount(), statResponse.getViewsCount());
        return statResponse;
    }
}
