package main.controllers;

import main.DTO.moderation.ResponseApi;
import main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    @Autowired
    public ApiAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseApi> authLogin(@RequestParam String email, @RequestParam String password) {
        return new ResponseEntity<>(userService.populateUserOnLogin(email, password), HttpStatus.OK);
    }

    @GetMapping(value = "/check")
    public ResponseEntity<ResponseApi> checkUser(@RequestParam int id) {
        return new ResponseEntity<>(userService.checkUserAuth(id), HttpStatus.OK);
    }

    @PostMapping(value = "/restore")
    public ResponseEntity<ResponseApi> restorePassword(@RequestParam String email, HttpServletRequest request) {
        return new ResponseEntity<>(userService.restorePassword(email, request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseApi> logout(@RequestParam int id) {
        return new ResponseEntity<>(userService.logout(id), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseApi> registerNewUser(@RequestParam String email, @RequestParam String name,
                                                       @RequestParam String password, @RequestParam String captcha,
                                                       @RequestParam String captcha_secret) {
        return new ResponseEntity<>(userService.register(email, name, password, captcha, captcha_secret), HttpStatus.OK);
    }
}
