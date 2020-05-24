package main.services;

import main.DTO.moderation.ResponseApi;
import main.DTO.moderation.UserModerationDto;
import main.mapper.UserMapper;
import main.model.User;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.lang.Math.toIntExact;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    protected Map<String, Integer> sessionIds = new HashMap<>();

    public UserModerationDto mapUserModeration(User user){
        return userMapper.toDtoModeration(user);
    }

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public UserDtoById getUserById(Long id){
//        return userMapper.toDtoById(userRepository.getOne(id));
//    }

    public ResponseApi<UserModerationDto> populateUserOnLogin(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            ResponseApi responseApi = ResponseApi.builder()
                    .result("false").build();
            return responseApi;
        }else if(!passwordEncoder.matches(password, userOptional.get().getPassword())){
            ResponseApi responseApi = ResponseApi.builder()
                    .result("false").build();
            return responseApi;
        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        sessionIds.put(sessionId, userOptional.get().getId());
        UserModerationDto userModerationDto = mapUserModeration(userOptional.get());
        if(userOptional.get().getIsModerator() == 1){
            userModerationDto.setModeration(true);
            userModerationDto.setSettings(true);
        }
        userModerationDto.setModerationCount(toIntExact(postRepository.countNewPostsToModerator()));
        ResponseApi responseApi = ResponseApi.builder()
                .result("true")
                .user(userModerationDto)
                .build();
        return responseApi;
    }

    public ResponseApi<UserModerationDto> checkUserAuth(int id){
        ResponseApi responseApi;
        if(sessionIds.containsValue(id)){
            User user = userRepository.getOne(id);
            UserModerationDto userModerationDto = mapUserModeration(user);
            if(user.getIsModerator() == 1){
                userModerationDto.setModeration(true);
                userModerationDto.setSettings(true);
            }
            userModerationDto.setModerationCount(toIntExact(postRepository.countNewPostsToModerator()));
            responseApi = ResponseApi.builder()
                    .result("true")
                    .user(userModerationDto)
                    .build();
        }else {
            responseApi = ResponseApi.builder()
                    .result("false").build();
        }
        return responseApi;
    }

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
        String stringId = String.valueOf(id);
        if(sessionIds.containsValue(id)){
            sessionIds.entrySet().removeIf(entry -> (stringId.equals(entry.getValue())));
        }
        return new ResponseApi().builder().result("true").build();
    }

    public ResponseApi register(String email, String name, String password, String captcha, String secretCode){
        ResponseApi responseApi;
        HashMap<String, String> errors = new HashMap<>(4);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getEmail().equals(email)){
                errors.put("email", "Email is already registered and/or incorrect");
            }
            if(user.getName().equals(name) || name.length() > 12 || name.length() < 3){
                errors.put("name", "Name is incorrect");
            }
            if(password.length() < 6){
                errors.put("password", "Password is less than 6 symbols");
            }
            if(!captcha.equals(secretCode)){
                errors.put("captcha", "Captcha code is incorrect");
            }
            responseApi = ResponseApi.builder()
                        .result("false").errors(errors).build();
        }else {
            if(name.length() > 12 || name.length() < 3){
                errors.put("name", "Name is incorrect");
            }
            if(password.length() < 6){
                errors.put("password", "Password is less than 6 symbols");
            }
            if(!captcha.equals(secretCode)){
                errors.put("captcha", "Captcha code is incorrect");
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
            }
        }
        return responseApi;
    }
}
