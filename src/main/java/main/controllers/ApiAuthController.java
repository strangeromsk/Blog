package main.controllers;

import main.DTO.UserDto;
import main.DTO.UserRegisterResponse;
import main.DTO.moderation.ResponseApi;
import main.services.CaptchaService;
import main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final CaptchaService captchaService;

    @Autowired
    public ApiAuthController(UserService userService, CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseApi> authLogin(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.populateUserOnLogin(userDto.getE_mail(), userDto.getPassword()), HttpStatus.OK);
    }

    @GetMapping(value = "/check")
    public ResponseEntity<ResponseApi> checkUser() {
//        String session = ((WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
//                .getSessionId();
        String session = RequestContextHolder.currentRequestAttributes().getSessionId();
        HashMap<String, Integer> sessionMap = new HashMap<>(2);
        sessionMap.put(session, 1);//?
        userService.setSessionIds(sessionMap);
        if(session == null || session.equals("")){
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        //int sessionId = Integer.parseInt(session);
        int userId = userService.getSessionIds().get(session);
        boolean userAuthorized = userService.getSessionIds().containsValue(userId);
        if(userAuthorized){
            return new ResponseEntity<>(userService.checkUserAuth(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result("false").build(), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/restore")
    public ResponseEntity<ResponseApi> restorePassword(@RequestBody UserDto userDto, HttpServletRequest request) {
        return new ResponseEntity<>(userService.restorePassword(userDto.getE_mail(), request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseApi> logout(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.logout(userDto.getId()), HttpStatus.OK);
    }

//    @PostMapping(value = "/register")
//    public ResponseEntity<ResponseApi> registerNewUser(@RequestParam String email, @RequestParam String name,
//                                                       @RequestParam String password, @RequestParam String captcha,
//                                                       @RequestParam String captcha_secret) {
//        return new ResponseEntity<>(userService.register(email, name, password, captcha, captcha_secret), HttpStatus.OK);
//    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseApi> register(@RequestBody UserRegisterResponse userRegisterResponse,
                                                       HttpServletRequest request) {
        String email = userRegisterResponse.getE_mail();
        String password = userRegisterResponse.getPassword();
        String name = userRegisterResponse.getName();
        String captcha = userRegisterResponse.getCaptcha();
        String captcha_secret = userRegisterResponse.getCaptchaSecret();
        return new ResponseEntity<>(userService.register(email, name, password, captcha, captcha_secret,request), HttpStatus.OK);
    }

    @GetMapping(value = "/captcha")
    public ResponseEntity<ResponseApi> captcha(HttpServletRequest request){
        return new ResponseEntity<>(captchaService.captchaGen(request), HttpStatus.OK);
    }
}
