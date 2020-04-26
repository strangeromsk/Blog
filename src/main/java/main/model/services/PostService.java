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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

@Service
@Scope(value = "session")
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private ApiPostController apiPostController;
    private PostDtoView postDtoView;
    private PostDto postDto;
    private Post post;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper,
                       PostDtoView postDtoView, PostDto postDto, Post post,
                       ApiPostController apiPostController) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postDtoView = postDtoView;
        this.postDto = postDto;
        this.post = post;
        this.apiPostController = apiPostController;
    }

    public PostDto save(PostDto postDto){
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto get(Long id){
        return postMapper.toDto(postRepository.getOne(id));
    }

    public void populateVars(PostDtoView postDtoView, Post post){
        postDtoView.setCount(postRepository.count());
        postDtoView.setPosts(postRepository.findAll().stream().filter(e ->
            e.getIsActive() == 1 && e.getStatus().equals(Post.Status.ACCEPTED))
                .map(e->get(e.getId()))
                .collect(Collectors.toCollection(ArrayList::new)));
              //  .collect(Collectors.toCollection(ArrayList::new)));
        //where to use mapper from post to postDTO?
    }
}
