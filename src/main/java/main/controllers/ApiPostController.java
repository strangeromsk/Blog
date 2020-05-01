package main.controllers;

import main.model.DTO.ModePostDto;
import main.model.DTO.PostDtoView;
import main.model.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/api/post")
    public ResponseEntity<PostDtoView> getAllPosts(@RequestParam int offset,
                                      @RequestParam int limit,
                                      @RequestParam ModePostDto mode) {
        if(offset < 0 || limit <= 0 || !(mode instanceof ModePostDto)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVars(offset, limit, mode), HttpStatus.OK);
    }
}
