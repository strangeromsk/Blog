package main.services;

import main.DTO.moderation.ResponseApi;
import main.DTO.moderation.UserModerationDto;
import main.mapper.UserMapper;
import main.model.User;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.toIntExact;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserModerationDto mapUserModeration(User user){
        return userMapper.toDtoModeration(user);
    }

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PostRepository postRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

//    public UserDtoById getUserById(Long id){
//        return userMapper.toDtoById(userRepository.getOne(id));
//    }

    public ResponseApi<UserModerationDto> populateUserOnLogin(String email, String password){
        Map<String, Integer> sessionIds = new HashMap<>();
        User user = userRepository.getUserByEmail(email, password);
        if(user == null){
            ResponseApi responseApi = ResponseApi.builder()
                    .result("false").build();
            return responseApi;
        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        sessionIds.put(sessionId, user.getId());
        UserModerationDto userModerationDto = mapUserModeration(user);
        if(user.getIsModerator() == 1){
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
}
