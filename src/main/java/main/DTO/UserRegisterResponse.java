package main.DTO;

import lombok.Data;

@Data
public class UserRegisterResponse {
    private String e_mail;
    private String password;
    private String name;
    private String captcha;
    private String captchaSecret;

}
