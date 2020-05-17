package main.services;

import main.DTO.PostDtoById.UserDtoById;
import main.mapper.UserMapper;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDtoById getUserById(Long id){
        return userMapper.toDtoById(userRepository.getOne(id));
    }
}
