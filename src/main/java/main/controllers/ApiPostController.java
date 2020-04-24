package main.controllers;

import main.model.DTO.ModePostDto;
import main.model.DTO.PostDtoView;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    @GetMapping(value = "/api/post")
    public PostDtoView getAllPosts(Integer offset, Integer limit, ModePostDto mode) {
//        HashMap<Integer, List<PostDto>> postsMap = new HashMap<>();
//        for (offset = 0; offset < limit; offset++){
//            postsMap.put(count, posts);
//        }
        PostDtoView postDtoView = new PostDtoView();

        return postDtoView;
    }
}
