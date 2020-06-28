package main.services;

import lombok.Getter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.toIntExact;

@Service
public class UserService {
    private final CaptchaService captchaService;
    private final CaptchaRepository captchaRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public boolean isModerator(int id){
        return userRepository.getOne(id).getIsModerator() == 1;
    }

    @Getter
    protected Map<String, Integer> sessionIds = new ConcurrentHashMap<>();

    public UserModerationDto mapUserModeration(User user){
        return userMapper.toDtoModeration(user);
    }

    @Autowired
    public UserService(CaptchaService captchaService, CaptchaRepository captchaRepository, UserMapper userMapper, UserRepository userRepository,
                       PostRepository postRepository, PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.captchaService = captchaService;
        this.captchaRepository = captchaRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    public User getUser(int userId){
        return userRepository.getOne(userId);
    }

    public ResponseEntity<ResponseApi> populateUserOnLogin(String email, String password){
        ResponseApi responseApi;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            responseApi = ResponseApi.builder()
                    .result("false").build();
            return new ResponseEntity<>(responseApi, HttpStatus.UNAUTHORIZED);
        }else if(!passwordEncoder.matches(password, userOptional.get().getPassword())) {
            responseApi = ResponseApi.builder()
                    .result("false").build();
            return new ResponseEntity<>(responseApi, HttpStatus.UNAUTHORIZED);
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
                    .result("true")
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
                .result("true")
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
            passwordResetEmail.setText(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link\n:" + appUrl + "/login/change-password/" + randomHash);

            responseApi = ResponseApi.builder()
                    .result("true").build();
        }else {
            responseApi = ResponseApi.builder()
                    .result("false").build();
        }
        return responseApi;
    }

    public ResponseApi logout (int id){
        //sessionIds.values().removeIf(value-> value == id);
        sessionIds.forEach((key, value) -> {
            if (value == id) {
                sessionIds.remove(key, value);
            }
        });
        return ResponseApi.builder().result("true").build();
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
                    .result("false").errors(errors).build();
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            errors.put("email", "Email is already registered and/or incorrect");
            responseApi = ResponseApi.builder()
                        .result("false").errors(errors).build();
            return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
        }else {
            if(name.length() > maxNameLength || name.length() < minNameLength){
                errors.put("name", "Name is incorrect");
            }
            if(password.length() < minPasswordLength){
                errors.put("password", "Password is less than 6 symbols");
            }
            responseApi = ResponseApi.builder()
                    .result("false").errors(errors).build();
            if(errors.size() == 0){
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(passwordEncoder.encode(password));
                user.setCode(secretCode);
                user.setRegTime(new Date());
                user.setIsModerator(0);
                userRepository.save(user);
                responseApi = ResponseApi.builder()
                        .result("true").build();
                return new ResponseEntity<>(responseApi, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changeMyProfile(UserMyProfileDto userMyProfileDto, User user){
        ResponseApi responseApi;
        int maxNameLength = 12;
        int minNameLength = 3;
        int minPasswordLength = 6;
        Optional<String> email = Optional.ofNullable(userMyProfileDto.getEmail());
        Optional<String> name = Optional.ofNullable(userMyProfileDto.getName());
        Optional<String> password = Optional.ofNullable(userMyProfileDto.getPassword());
        byte removePhoto = userMyProfileDto.getRemovePhoto();
        boolean firstCond = email.isPresent() && name.isPresent();
        boolean secondCond = email.isPresent() && name.isPresent() && password.isPresent();
        boolean thirdCond = email.isPresent() && name.isPresent() && password.isPresent() && removePhoto == 0;
        boolean forthCond = email.isPresent() && name.isPresent() && removePhoto == 1;
        Map<String, String> errors = new HashMap<>(8);

        if(email.isPresent() && userRepository.findByEmail(email.get()).isPresent()){
            errors.put("email", "Email is already registered and/or incorrect");
        }
        if(name.isPresent() && (name.get().length() > maxNameLength || name.get().length() < minNameLength)){
            errors.put("name", "Name is incorrect");
        }
        if(password.isPresent() && password.get().length() < minPasswordLength){
            errors.put("password", "Password is less than 6 symbols");
        }

        responseApi = ResponseApi.builder()
                .result("false").errors(errors).build();
        if(errors.size() == 0){
            if(forthCond){
                user.setEmail(email.get());
                user.setName(name.get());
                user.setPhoto("");
            }else if(thirdCond){
                user.setEmail(email.get());
                user.setName(name.get());
                user.setPassword(passwordEncoder.encode(password.get()));
            }else if(secondCond){
                user.setEmail(email.get());
                user.setName(name.get());
                user.setPassword(passwordEncoder.encode(password.get()));
            }else if(firstCond){
                user.setEmail(email.get());
                user.setName(name.get());
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userRepository.save(user);
            responseApi = ResponseApi.builder()
                    .result("true").build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseApi> changeMyProfileWithPhoto(MultipartFile photo, String name, String email,
                                                                String password, int removePhoto, User user){
        ResponseApi responseApi;
        int maxNameLength = 12;
        int minNameLength = 3;
        int minPasswordLength = 6;
        Optional<String> emailOptional = Optional.ofNullable(email);
        Optional<String> nameOptional = Optional.ofNullable(name);
        Optional<String> passwordOptional = Optional.ofNullable(password);

        boolean firstCond = emailOptional.isPresent() && nameOptional.isPresent();
        boolean secondCond = emailOptional.isPresent() && nameOptional.isPresent() && passwordOptional.isPresent();
        boolean thirdCond = emailOptional.isPresent() && nameOptional.isPresent() && passwordOptional.isPresent() && removePhoto == 0;
        boolean forthCond = emailOptional.isPresent() && nameOptional.isPresent() && photo.isEmpty() && removePhoto == 1;
        Map<String, String> errors = new HashMap<>(8);

        if(emailOptional.isPresent() && userRepository.findByEmail(emailOptional.get()).isPresent()){
            errors.put("email", "Email is already registered and/or incorrect");
        }
        if(nameOptional.isPresent() && (nameOptional.get().length() > maxNameLength || nameOptional.get().length() < minNameLength)){
            errors.put("name", "Name is incorrect");
        }
        if(passwordOptional.isPresent() && passwordOptional.get().length() < minPasswordLength){
            errors.put("password", "Password is less than 6 symbols");
        }

        responseApi = ResponseApi.builder()
                .result("false").errors(errors).build();
        if(errors.size() == 0){
            if(forthCond){
                user.setEmail(emailOptional.get());
                user.setName(nameOptional.get());
                user.setPhoto("");
            }else if(thirdCond){
                user.setEmail(emailOptional.get());
                user.setName(nameOptional.get());
                user.setPassword(passwordEncoder.encode(passwordOptional.get()));

                user.setPhoto(fileStorageService.storeFileResized(photo));
            }else if(secondCond){
                user.setEmail(emailOptional.get());
                user.setName(nameOptional.get());
                user.setPassword(passwordEncoder.encode(passwordOptional.get()));
            }else if(firstCond){
                user.setEmail(emailOptional.get());
                user.setName(nameOptional.get());
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userRepository.save(user);
            responseApi = ResponseApi.builder()
                    .result("true").build();
            return new ResponseEntity<>(responseApi, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }

    public StatResponse myStatistics(int userId){
        List<Post> postsList = postRepository.findAllPostsByUserId(userId);
        StatResponse statResponse = new StatResponse();
        if(postsList.size() != 0){
            statResponse.setPostsCount(postsList.size());
            statResponse.setLikesCount(postsList.stream().map(e->e.getPostVotes().stream().filter(k->k.getValue() == 1)).count());
            statResponse.setDislikesCount(postsList.stream().map(e->e.getPostVotes().stream().filter(k->k.getValue() == -1)).count());
            statResponse.setViewsCount(postsList.stream().map(Post::getViewCount).count());
            statResponse.setFirstPublication(postsList.stream().map(Post::getTime).min(Date::compareTo).get());
        }else {
            statResponse.setPostsCount(0);
            statResponse.setLikesCount(0);
            statResponse.setDislikesCount(0);
            statResponse.setViewsCount(0);
        }
        return statResponse;
    }

    public StatResponse allStatistics(){
        List<Post> postsList = postRepository.findAll();
        StatResponse statResponse = new StatResponse();
        if(postsList.size() != 0){
            statResponse.setPostsCount(postsList.size());
            statResponse.setLikesCount(postsList.stream().map(e->e.getPostVotes().stream().filter(k->k.getValue() == 1)).count());
            statResponse.setDislikesCount(postsList.stream().map(e->e.getPostVotes().stream().filter(k->k.getValue() == -1)).count());
            statResponse.setViewsCount(postsList.stream().map(Post::getViewCount).count());
            statResponse.setFirstPublication(postsList.stream().map(Post::getTime).min(Date::compareTo).get());
        }else {
            statResponse.setPostsCount(0);
            statResponse.setLikesCount(0);
            statResponse.setDislikesCount(0);
            statResponse.setViewsCount(0);
        }
        return statResponse;
    }
}
