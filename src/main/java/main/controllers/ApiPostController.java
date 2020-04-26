package main.controllers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import main.model.DTO.ModePostDto;
import main.model.DTO.PostDtoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {
    @Getter
    @Setter
    private int offset = 0;
    @Getter
    @Setter
    private int limit = 0;
    @Getter
    @Setter
    private ModePostDto modePostDto;

    @Getter
    @Setter
    @Autowired
    private PostDtoView postDtoView;

    @GetMapping(value = "/api/post")
    public PostDtoView getAllPosts(@RequestParam int offset,
                                   @RequestParam int limit,
                                   @RequestParam ModePostDto mode) {
        this.offset = offset;
        this.limit = limit;
        this.modePostDto = mode;
//        HashMap<Integer, List<PostDto>> postsMap = new HashMap<>();
//        for (offset = 0; offset < limit; offset++){
//            postsMap.put(count, posts);
//        }

        return postDtoView;
    }
}
