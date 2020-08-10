package main.controllers;

import main.API.RequestApi;
import main.API.ResponseApi;
import main.DTO.ModePostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
import main.model.Post;
import main.model.User;
import main.services.PostServiceImpl;
import main.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@RequestMapping("/api/post")
@RestController
public class ApiPostController {
    private final PostServiceImpl postServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public ApiPostController(PostServiceImpl postServiceImpl, UserServiceImpl userServiceImpl) {
        this.postServiceImpl = postServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<PostDtoView> getAllPosts(@RequestParam int offset,
                                                   @RequestParam int limit,
                                                   @RequestParam ModePostDto mode) {
        Optional<PostDtoView> postsList = Optional.ofNullable(postServiceImpl.populateVars(offset, limit, mode));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postServiceImpl.populateVars(offset, limit, mode), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new PostDtoView(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PostDtoView> getSearchPosts(@RequestParam int offset,
                                                      @RequestParam int limit,
                                                      @RequestParam String query) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postServiceImpl.populateSearchVars(offset, limit, query));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postServiceImpl.populateSearchVars(offset, limit, query), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new PostDtoView(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDtoById> getPostById(@PathVariable Integer id) {
        if(id == null || id < 0){
            return new ResponseEntity<>(new PostDtoById(), HttpStatus.OK);
        }
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        Optional<PostDtoById> post;
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            post = Optional.ofNullable(postServiceImpl.populateVarsByPostIdWithUser(id, user));
            if(post.isPresent()){
                return new ResponseEntity<>(postServiceImpl.populateVarsByPostIdWithUser(id, user), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new PostDtoById(), HttpStatus.OK);
            }
        }
        post = Optional.ofNullable(postServiceImpl.populateVarsByPostId(id));
        if(post.isPresent()){
            return new ResponseEntity<>(postServiceImpl.populateVarsByPostId(id), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new PostDtoById(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/byDate")
    public ResponseEntity<PostDtoView> getPostsWithExactDate(@RequestParam int offset,
                                                             @RequestParam int limit,
                                                             @RequestParam String date) {
        if(offset < 0 || limit <= 0 || date == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postServiceImpl.populateVarsWithExactDate(offset, limit, date));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postServiceImpl.populateVarsWithExactDate(offset, limit, date), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new PostDtoView(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/byTag")
    public ResponseEntity<PostDtoView> getPostsWithTag(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String tag) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<PostDtoView> postsList = Optional.ofNullable(postServiceImpl.populateTagVars(offset, limit, tag));
        if(postsList.isPresent()){
            return new ResponseEntity<>(postServiceImpl.populateTagVars(offset, limit, tag), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new PostDtoView(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/my")
    public ResponseEntity<PostDtoView> getMyPosts(@RequestParam int offset,
                                                  @RequestParam int limit,
                                                  @RequestParam Post.Status status) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        return userId.map(integer -> new ResponseEntity<>(postServiceImpl.populateMyVars(integer, offset, limit, status), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping(value = "/moderation")
    public ResponseEntity<PostDtoView> getAllPostsWithModeration(@RequestParam int offset,
                                                                 @RequestParam int limit,
                                                                 @RequestParam String status) {
        if(offset < 0 || limit <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean isModerator = userServiceImpl.isModerator(userId.get());
            if(isModerator){
                return new ResponseEntity<>(postServiceImpl.populateVarsModeration(offset, limit, status), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping
    public ResponseEntity<ResponseApi> makeNewPost(@RequestBody RequestApi post) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return postServiceImpl.makeNewPost(post, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseApi> changePostById(@PathVariable Integer id, @RequestBody RequestApi post) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return postServiceImpl.changePost(id, post, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/like")
    public ResponseEntity<ResponseApi> makeNewLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return postServiceImpl.makeNewLike(requestApi.getPostId(), user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/dislike")
    public ResponseEntity<ResponseApi> makeNewDisLike(@RequestBody RequestApi requestApi) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return postServiceImpl.makeNewDisLike(requestApi.getPostId(), user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
