package main.services.interfaces;

import main.API.ResponseApi;
import main.DTO.StatResponse;
import main.DTO.UserMyProfileDto;
import main.DTO.UserRegisterResponse;
import main.DTO.moderation.UserModerationDto;
import main.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseEntity<ResponseApi> populateUserOnLogin(String email, String password);
    ResponseApi<UserModerationDto> checkUserAuth(int id);
    ResponseApi restorePassword(String email, HttpServletRequest request);
    ResponseEntity<ResponseApi> register(UserRegisterResponse userRegisterResponse);
    ResponseEntity<ResponseApi> changeMyProfile(UserMyProfileDto userMyProfileDto, User user);
    ResponseEntity<ResponseApi> changeMyProfileWithPhoto(MultipartFile photo, String name, String email,
                                                         String password, int removePhoto, User user,
                                                         HttpServletRequest request);
    StatResponse myStatistics(int userId);
    StatResponse allStatistics();
}
