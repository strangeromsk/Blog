package main.controllers;

import main.DTO.ModePostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
import main.DTO.TagDto;
import main.services.PostService;
import main.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiPostController {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @GetMapping(value = "/post")
    public ResponseEntity<PostDtoView> getAllPosts(@RequestParam int offset,
                                                   @RequestParam int limit,
                                                   @RequestParam ModePostDto mode) {
        return new ResponseEntity<>(postService.populateVars(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping(value = "/post/search")
    public ResponseEntity<PostDtoView> getSearchPosts(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String query) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateSearchVars(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<PostDtoById> getPostById(@PathVariable Integer id) {
        if(id == null || id < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVarsByPostId(id), HttpStatus.OK);
    }

    @GetMapping(value = "/post/byDate")
    public ResponseEntity<PostDtoView> getPostsWithExactDate(@RequestParam int offset,
                                                             @RequestParam int limit,
                                                             @RequestParam String date) {
        if(offset < 0 || limit <= 0 || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVarsWithExactDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/post/byTag")
    public ResponseEntity<PostDtoView> getPostsWithTag(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String tag) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateTagVars(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "/post/tag")
    public ResponseEntity<List<TagDto>> getTagsByQuery(@RequestParam String query) {
        return new ResponseEntity<>(tagService.getTags(query), HttpStatus.OK);
    }

    @GetMapping(value = "/post/my")
    public ResponseEntity<PostDtoView> getMyPosts(@RequestParam int offset,
                                                   @RequestParam int limit,
                                                   @RequestParam ModePostDto mode) {
        if(SecurityContextHolder.getContext().getAuthentication() == null &&
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
//        String session = ((WebAuthenticationDetails) SecurityContextHolder
//                .getContext().getAuthentication().getDetails()).getSessionId();
//        if(session == null || session.equals("")){
//            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
//        }
        return new ResponseEntity<>(postService.populateMyVars(offset, limit, mode), HttpStatus.OK);
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
