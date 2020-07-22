package main.services.interfaces;

import main.API.ResponseApi;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaService {
    ResponseApi generateCaptcha(HttpServletRequest request);
}
