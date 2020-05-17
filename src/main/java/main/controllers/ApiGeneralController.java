package main.controllers;

import main.DTO.CalendarDto.CalendarDto;
import main.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class ApiGeneralController {
    private final PostService postService;
    @Autowired
    public ApiGeneralController(PostService postService) {
        this.postService = postService;
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

}


