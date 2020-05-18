package main.controllers;

import main.DTO.moderation.ResponseApi;
import main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApiAuthController {

    private final UserService userService;
    @Autowired
    public ApiAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/api/auth/login")
    public ResponseEntity<ResponseApi> getCalendar(@RequestParam String email, @RequestParam String password) {
        return new ResponseEntity<>(userService.populateUserOnLogin(email, password), HttpStatus.OK);
    }
}
