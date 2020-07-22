package main.controllers;

import main.API.ResponseApi;
import main.DTO.UserDto;
import main.DTO.UserRegisterResponse;
import main.model.User;
import main.services.CaptchaServiceImpl;
import main.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserServiceImpl userServiceImpl;
    private final CaptchaServiceImpl captchaServiceImpl;

    @Autowired
    public ApiAuthController(UserServiceImpl userServiceImpl, CaptchaServiceImpl captchaServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.captchaServiceImpl = captchaServiceImpl;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseApi> authLogin(@RequestBody UserDto userDto) {
        return userServiceImpl.populateUserOnLogin(userDto.getEmail(), userDto.getPassword());
    }

    @GetMapping(value = "/check")
    public ResponseEntity<ResponseApi> checkUser() {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        return userId.<ResponseEntity<ResponseApi>>map(integer -> new ResponseEntity<>(userServiceImpl.checkUserAuth(integer), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(ResponseApi.builder().result(false).build(), HttpStatus.OK));
    }

    @PostMapping(value = "/restore")
    public ResponseEntity<ResponseApi> restorePassword(@RequestBody User user, HttpServletRequest request) {
        return new ResponseEntity<>(userServiceImpl.restorePassword(user.getEmail(), request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseApi> logout() {
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<Integer> userId = Optional.ofNullable(userServiceImpl.getSessionIds().get(session));
        if(userId.isPresent()){
            userServiceImpl.getSessionIds().remove(session);
        }
        return new ResponseEntity<>(ResponseApi.builder().result(true).build(), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseApi> register(@RequestBody UserRegisterResponse userRegisterResponse) {
        return userServiceImpl.register(userRegisterResponse);
    }

    @GetMapping(value = "/captcha")
    public ResponseEntity<ResponseApi> captcha(HttpServletRequest request){
        return new ResponseEntity<>(captchaServiceImpl.generateCaptcha(request), HttpStatus.OK);
    }
}
