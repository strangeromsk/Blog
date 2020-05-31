package main.controllers;

import main.DTO.CalendarDto.CalendarDto;
import main.DTO.moderation.ResponseApi;
import main.model.User;
import main.services.PostCommentService;
import main.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class ApiGeneralController {
    private final PostService postService;
    private final PostCommentService postCommentService;
    @Autowired
    public ApiGeneralController(PostService postService, PostCommentService postCommentService) {
        this.postService = postService;
        this.postCommentService = postCommentService;
    }

    @GetMapping(value = "/api/calendar")
    public ResponseEntity<CalendarDto> getCalendar(@RequestParam int year) {
        if(String.valueOf(year).length() != 4){
            Date date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            return new ResponseEntity<>(postService.populateCalendarVars(year), HttpStatus.OK);
        }
        return new ResponseEntity<>(postService.populateCalendarVars(year), HttpStatus.OK);
    }

    @PostMapping(value = "/api/comment")
    public ResponseEntity<ResponseApi> makeNewComment(@AuthenticationPrincipal User user,
                                                      int parentId, int postId, String text){
        return new ResponseEntity<>(postCommentService.makeNewComment(user, parentId, postId, text), HttpStatus.OK);
    }
}


