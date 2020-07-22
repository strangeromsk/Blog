package main.controllers;

import main.API.RequestApi;
import main.API.ResponseApi;
import main.DTO.CalendarDto;
import main.DTO.PostDtoById.CommentDtoById;
import main.DTO.SettingsResponse;
import main.DTO.StatResponse;
import main.DTO.UserMyProfileDto;
import main.model.User;
import main.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    private final PostServiceImpl postServiceImpl;
    private final TagServiceImpl tagServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final SettingsServiceImpl settingsServiceImpl;
    private final PostCommentServiceImpl postCommentServiceImpl;
    private final FileStorageServiceImpl fileStorageService;

    @Autowired
    public ApiGeneralController(PostServiceImpl postServiceImpl, TagServiceImpl tagServiceImpl, UserServiceImpl userServiceImpl,
                                SettingsServiceImpl settingsServiceImpl, PostCommentServiceImpl postCommentServiceImpl,
                                FileStorageServiceImpl fileStorageService) {
        this.postServiceImpl = postServiceImpl;
        this.tagServiceImpl = tagServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.settingsServiceImpl = settingsServiceImpl;
        this.postCommentServiceImpl = postCommentServiceImpl;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<CalendarDto> getCalendar(@RequestParam(required = false) Integer year) {
        if(String.valueOf(year).length() != 4){
            Date date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            return new ResponseEntity<>(postServiceImpl.populateCalendarVars(year), HttpStatus.OK);
        }
        return new ResponseEntity<>(postServiceImpl.populateCalendarVars(year), HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<ResponseApi> makeNewComment(@RequestBody CommentDtoById commentDtoById){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            return postCommentServiceImpl.makeNewComment(userId.get(),
                        commentDtoById.getParentId(),
                        commentDtoById.getPostId(),
                        commentDtoById.getText());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity getTagsByQuery(@RequestParam(required = false) String query) {
        return new ResponseEntity<>(tagServiceImpl.getTags(query), HttpStatus.OK);
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<SettingsResponse> getSettings() {
        return settingsServiceImpl.getSettings();
    }

    @PutMapping(value = "/settings")
    public ResponseEntity changeSettings(@RequestBody SettingsResponse settingsResponse) {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            boolean isModerator = userServiceImpl.isModerator(userId.get());
            if(isModerator){
                return settingsServiceImpl.changeSettings(settingsResponse);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String URIAndFilename = "/upload/" + fileName;
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            return new ResponseEntity<>(URIAndFilename, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/moderation")
    public ResponseEntity moderation(@RequestBody RequestApi requestApi){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            boolean isModerator = userServiceImpl.isModerator(userId.get());
            if(isModerator){
                return new ResponseEntity<>(postServiceImpl.postModeration(requestApi, user), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/profile/my")
    public ResponseEntity changeMyProfile(@RequestBody(required = false) UserMyProfileDto userMyProfileDto){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return userServiceImpl.changeMyProfile(userMyProfileDto, user);
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
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            User user = userServiceImpl.getUser(userId.get());
            return userServiceImpl.changeMyProfileWithPhoto(photo, name, email, password, removePhoto, user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/statistics/my")
    public ResponseEntity<StatResponse> myStatistics(){
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        return userId.map(integer -> new ResponseEntity<>(userServiceImpl.myStatistics(integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping(value = "/statistics/all")
    public ResponseEntity<StatResponse> allStatistics(){
        boolean statIsPublic = settingsServiceImpl.getStatIsPublic();
        if(statIsPublic){
            return new ResponseEntity<>(userServiceImpl.allStatistics(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


