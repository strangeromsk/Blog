package main.model.services;

import main.mapper.PostMapper;
import main.mapper.UserMapper;
import main.model.DTO.ModePostDto;
import main.model.DTO.PostDto;
import main.model.DTO.PostDtoView;
import main.model.Post;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       UserRepository userRepository, UserMapper userMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public PostDto save(PostDto postDto){
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto get(Long id){
        return postMapper.toDto(postRepository.getOne(id));
    }

    public PostDtoView populateVars(int offset, int limit, ModePostDto mode){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostDtoView postDtoView = new PostDtoView();
        postDtoView.setCount(postRepository.findPost(pageable).size());
        List<Post> list;
        if(mode.equals(ModePostDto.recent)){
            list = postRepository.findPostByDateAsc(pageable);
        }else if(mode.equals(ModePostDto.early)){
            list = postRepository.findPostByDateDesc(pageable);
        }else{
            list = postRepository.findPost(pageable);
        }
        postDtoView.setPosts(list.stream()
                .map(e->get(e.getId()))
                .collect(Collectors.toList()));
        return postDtoView;
    }
}
