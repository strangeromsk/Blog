package main.controllers;

import main.API.RequestApi;
import main.DTO.*;
import main.API.ResponseApi;
import main.DTO.PostDtoById.CommentDtoById;
import main.model.User;
import main.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;
    private final SettingsService settingsService;
    private final PostCommentService postCommentService;
    private final FileStorageService fileStorageService;

    @Autowired
    public ApiGeneralController(PostService postService, TagService tagService, UserService userService,
                                SettingsService settingsService, PostCommentService postCommentService,
                                FileStorageService fileStorageService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
        this.settingsService = settingsService;
        this.postCommentService = postCommentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<CalendarDto> getCalendar(@RequestParam(required = false) Integer year) {
        if(String.valueOf(year).length() != 4){
            Date date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            return new ResponseEntity<>(postService.populateCalendarVars(year), HttpStatus.OK);
        }
        return new ResponseEntity<>(postService.populateCalendarVars(year), HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<ResponseApi> makeNewComment(@RequestBody CommentDtoById commentDtoById){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            return postCommentService.makeNewComment(userId.get(),
                        commentDtoById.getParentId(),
                        commentDtoById.getPostId(),
                        commentDtoById.getText());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity getTagsByQuery(@RequestParam(required = false) String query) {
        return new ResponseEntity<>(tagService.getTags(query), HttpStatus.OK);
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<SettingsResponse> getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping(value = "/settings")
    public ResponseEntity changeSettings(@RequestBody SettingsResponse settingsResponse) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean isModerator = userService.isModerator(userId.get());
            if(isModerator){
                return settingsService.changeSettings(settingsResponse);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String URIAndFilename = "/upload/" + fileName;
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            return new ResponseEntity<>(URIAndFilename, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/moderation")
    public ResponseEntity moderation(@RequestBody RequestApi requestApi){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            boolean isModerator = userService.isModerator(userId.get());
            if(isModerator){
                return postService.postModeration(requestApi, user);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/profile/my")
    public ResponseEntity changeMyProfile(@RequestBody(required = false) UserMyProfileDto userMyProfileDto){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return userService.changeMyProfile(userMyProfileDto, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity changeMyProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                   @RequestParam String name,
                                                   @RequestParam String email,
                                                   @RequestParam(required = false, defaultValue = "") String password,
                                                   @RequestParam(required = false, defaultValue = "0") int removePhoto){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userService.getUser(userId.get());
            return userService.changeMyProfileWithPhoto(photo, name, email, password, removePhoto, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/statistics/my")
    public ResponseEntity<StatResponse> myStatistics(){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        return userId.map(integer -> new ResponseEntity<>(userService.myStatistics(integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping(value = "/statistics/all")
    public ResponseEntity<StatResponse> allStatistics(){
        boolean statIsPublic = settingsService.getStatIsPublic();
        if(statIsPublic){
            return new ResponseEntity<>(userService.allStatistics(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


