package main.controllers;

import main.DTO.CalendarDto;
import main.API.ResponseApi;
import main.DTO.SettingsResponse;
import main.DTO.StatResponse;
import main.DTO.TagDto;
import main.configuration.FileStorageProperties;
import main.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<ResponseApi> makeNewComment(int parentId, int postId, String text){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return new ResponseEntity<>(postCommentService.makeNewComment(userId.get(), parentId, postId, text), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<List<TagDto>> getTagsByQuery(@RequestParam(required = false) String query) {
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
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            boolean isModerator = userService.isModerator(userId.get());
            if(userAuthorized && isModerator){
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
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return new ResponseEntity<>(URIAndFilename, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/statistics/my")
    public ResponseEntity<StatResponse> myStatistics(){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
            if(userAuthorized){
                return new ResponseEntity<>(userService.myStatistics(userId.get()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//    @GetMapping(value = "/statistics/all")
//    public ResponseEntity<StatResponse> allStatistics(){
//        int statIsPublic = settingsService.getStatIsPublic();
//        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
//        Optional<Integer> userId = Optional.ofNullable(userService.getSessionIds().get(session));
//        if(userId.isPresent()){
//            boolean userAuthorized = userService.getSessionIds().containsValue(userId.get());
//            if(userAuthorized && statIsPublic == 1){
//                return new ResponseEntity<>(userService.myStatistics(userId.get()), HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
}


