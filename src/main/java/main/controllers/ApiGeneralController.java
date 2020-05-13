package main.controllers;

import main.DTO.PostDtoView;
import main.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {
    private final PostService postService;
    @Autowired
    public ApiGeneralController(PostService postService) {
        this.postService = postService;
    }
//    @GetMapping(value = "/api/calendar")
//    public ResponseEntity<PostDtoView> getCalendar(@RequestParam int year) {
//        if(String.valueOf(year).length() != 4){
//            return new ResponseEntity<>();
//        }
//        return new ResponseEntity<>(), HttpStatus.OK);
//
//    }

}


