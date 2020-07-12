package main.controllers;

import main.API.RequestApi;
import main.API.ResponseApi;
import main.DTO.ModePostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
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
        Optional<PostDtoView> postsList = Optional.ofNullable(postService.populateVars(offset, limit, mode));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postService.populateVars(offset, limit, mode), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/post/search")
    public ResponseEntity<PostDtoView> getSearchPosts(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String query) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postService.populateSearchVars(offset, limit, query));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postService.populateSearchVars(offset, limit, query), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/post/{id}")
    public ResponseEntity<PostDtoById> getPostById(@PathVariable Integer id) {
        if(id == null || id < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        Optional<PostDtoById> post;
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            post = Optional.ofNullable(postService.populateVarsByPostIdWithUser(id, user));
            if(post.isPresent()){
                return new ResponseEntity<>(postService.populateVarsByPostIdWithUser(id, user), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        post = Optional.ofNullable(postService.populateVarsByPostId(id));
        if(post.isPresent()){
            return new ResponseEntity<>(postService.populateVarsByPostId(id), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/post/byDate")
    public ResponseEntity<PostDtoView> getPostsWithExactDate(@RequestParam int offset,
                                                             @RequestParam int limit,
                                                             @RequestParam String date) {
        if(offset < 0 || limit <= 0 || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postService.populateVarsWithExactDate(offset, limit, date));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postService.populateVarsWithExactDate(offset, limit, date), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/post/byTag")
    public ResponseEntity<PostDtoView> getPostsWithTag(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String tag) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postService.populateTagVars(offset, limit, tag));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postService.populateTagVars(offset, limit, tag), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/post/my")
    public ResponseEntity<PostDtoView> getMyPosts(@RequestParam int offset,
                                                  @RequestParam int limit,
                                                  @RequestParam Post.Status status) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        return userId.map(integer -> new ResponseEntity<>(postService.populateMyVars(integer, offset, limit, status), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping(value = "/post/moderation")
    public ResponseEntity<PostDtoView> getAllPostsWithModeration(@RequestParam int offset,
                                                                 @RequestParam int limit,
                                                                 @RequestParam String status) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean isModerator = userService.isModerator(userId.get());
            if(isModerator){
                return new ResponseEntity<>(postService.populateVarsModeration(offset, limit, status), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<ResponseApi> makeNewPost(@RequestBody Post post) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return postService.makeNewPost(post, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping(value = "/post/{id}")
    public ResponseEntity<ResponseApi> changePostById(@PathVariable Integer id, @RequestBody RequestApi post) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return postService.changePost(id, post, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post/like")
    public ResponseEntity<ResponseApi> makeNewLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return postService.makeNewLike(requestApi.getPostId(), user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/post/dislike")
    public ResponseEntity<ResponseApi> makeNewDisLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return postService.makeNewLike(requestApi.getPostId(), user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
