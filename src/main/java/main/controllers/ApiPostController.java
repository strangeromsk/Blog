package main.controllers;

import main.model.DTO.PostDTO;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiPostController {
    public enum mode{
        RECENT, POPULAR, BEST, EARLY
    }

    @Autowired
    private PostRepository postRepository;

    @GetMapping(value = "/api/post")
    public List<HashMap<Integer,PostDTO>> getAllPosts()
    {
        Iterable<PostDTO> postIterable = postRepository.findAll();
        List<HashMap<Integer,PostDTO>> postsEnd = new ArrayList<>();
        HashMap<Integer, PostDTO> posts = new HashMap<>();

        int offset;
        int limit;
        int count = posts.size();
        for(PostDTO postDTO : postIterable) {
            posts.put(count, postDTO);
        }
        postsEnd.add(posts);
        return postsEnd;
    }
}
