package main.model.services;

import main.controllers.ApiPostController;
import main.mapper.PostMapper;
import main.model.DTO.PostDto;
import main.model.DTO.PostDtoView;
import main.model.Post;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Service

public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private ApiPostController apiPostController;
    private PostDtoView postDtoView;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       PostDtoView postDtoView,
                       ApiPostController apiPostController) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postDtoView = postDtoView;
        this.apiPostController = apiPostController;
    }

    public PostDto save(PostDto postDto){
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto get(Long id){
        return postMapper.toDto(postRepository.getOne(id));
    }

    public PostDtoView populateVars(){
        postDtoView.setCount(postRepository.count());
        postDtoView.setPosts(postRepository.findAll().stream().filter(e ->
                e.getIsActive() == 1 &&
                e.getStatus().equals(Post.Status.ACCEPTED) &&
                e.getTime().before(new Date()))
           .map(e->get(e.getId()))
           .skip(apiPostController.getOffset())
           .limit(apiPostController.getLimit())
           //     .peek(e->Comparator.comparing(e.getCommentCount(), Comparator.naturalOrder()))
           //     .sorted(e->Comparator.comparing(e.getCommentCount(),Comparator.reverseOrder()))
           .collect(Collectors.toCollection(ArrayList::new)));
        return postDtoView;
    }
}
