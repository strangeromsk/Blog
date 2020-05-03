package main.controllers;

import main.DTO.ModePostDto;
import main.DTO.PostDTOById.PostDtoById;
import main.DTO.PostDtoView;
import main.DTO.moderation.PostDtoViewModeration;
import main.model.Post;
import main.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    @GetMapping(value = "/api/post/search")
    public ResponseEntity<PostDtoView> getSearchPosts(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String query) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateSearchVars(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/{id}")
    public ResponseEntity<PostDtoById> getPostById(@PathVariable Integer id) {
        if(id == null || id < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVarsByPostId(id), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/byDate")
    public ResponseEntity<PostDtoView> getPostsWithExactDate(@RequestParam int offset,
                                                             @RequestParam int limit,
                                                             @RequestParam Date date) {
        if(offset < 0 || limit <= 0 || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVarsWithExactDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/api/post/byTag")
    public ResponseEntity<PostDtoView> getPostsWithTag(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String tag) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateTagVars(offset, limit, tag), HttpStatus.OK);
    }

//    @GetMapping(value = "/api/post/moderation")
//    public ResponseEntity<PostDtoViewModeration> getAllPostsWithModeration(@RequestParam int offset,
//                                                                           @RequestParam int limit,
//                                                                           @RequestParam Post.Status status) {
//        if(offset < 0 || limit <= 0 || !status.equals(Post.Status.NEW)){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(postService.populateVarsModeraton(offset, limit, status), HttpStatus.OK);
//    }
}
