package main.controllers;

import main.API.RequestApi;
import main.DTO.ModePostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
import main.DTO.TagDto;
import main.API.ResponseApi;
import main.model.Post;
import main.model.User;
import main.services.PostService;
import main.services.TagService;
import main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@RequestMapping("/api")
@RestController
public class ApiPostController {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/post")
    public ResponseEntity<PostDtoView> getAllPosts(@RequestParam int offset,
                                                   @RequestParam int limit,
                                                   @RequestParam ModePostDto mode) {
//        int offset = requestApi.getOffset();
//        int limit = requestApi.getLimit();
//        ModePostDto mode = requestApi.getMode();
        return new ResponseEntity<>(postService.populateVars(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping(value = "/post/search")
    public ResponseEntity<PostDtoView> getSearchPosts(@RequestBody RequestApi requestApi) {
        int offset = requestApi.getOffset();
        int limit = requestApi.getLimit();
        String query = requestApi.getQuery();
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
    public ResponseEntity<PostDtoView> getPostsWithExactDate(@RequestBody RequestApi requestApi) {
        int offset = requestApi.getOffset();
        int limit = requestApi.getLimit();
        String date = requestApi.getDate();
        if(offset < 0 || limit <= 0 || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateVarsWithExactDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "/post/byTag")
    public ResponseEntity<PostDtoView> getPostsWithTag(@RequestBody RequestApi requestApi) {
        int offset = requestApi.getOffset();
        int limit = requestApi.getLimit();
        String tag = requestApi.getTag();
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postService.populateTagVars(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "/post/my")
    public ResponseEntity<PostDtoView> getMyPosts(@RequestParam int offset,
                                                  @RequestParam int limit,
                                                  @RequestParam ModePostDto mode) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return new ResponseEntity<>(postService.populateMyVars(userId.get(), offset, limit, mode), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/post/moderation")
    public ResponseEntity<PostDtoView> getAllPostsWithModeration(@RequestParam int offset,
                                                                 @RequestParam int limit,
                                                                 @RequestParam Post.Status status) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            boolean isModerator = userService.isModerator(userId.get());
            if(userAuthorized && isModerator){
                return new ResponseEntity<>(postService.populateVarsModeration(userId.get(), offset, limit, status), HttpStatus.OK);
            }
        }
        if(offset < 0 || limit <= 0 || !status.equals(Post.Status.NEW)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<ResponseApi> makeNewPost(@RequestBody Post post) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return postService.makeNewPost(post, user);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post/like")
    public ResponseEntity<ResponseApi> makeNewLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return postService.makeNewLike(requestApi.getPostId(), user);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post/dislike")
    public ResponseEntity<ResponseApi> makeNewDisLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return postService.makeNewLike(requestApi.getPostId(), user);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
