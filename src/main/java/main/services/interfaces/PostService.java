package main.services.interfaces;

import main.API.RequestApi;
import main.API.ResponseApi;
import main.DTO.CalendarDto;
import main.DTO.ModePostDto;
import main.DTO.PostDtoById.PostDtoById;
import main.DTO.PostDtoView;
import main.model.Post;
import main.model.User;
import org.springframework.http.ResponseEntity;

public interface PostService {
    PostDtoView populateVars(int offset, int limit, ModePostDto mode);
    PostDtoView populateSearchVars(int offset, int limit, String query);
    PostDtoById populateVarsByPostIdWithUser(int id, User user);
    PostDtoById populateVarsByPostId(int id);
    PostDtoView populateVarsWithExactDate(int offset, int limit, String date);
    PostDtoView populateTagVars(int offset, int limit, String tag);
    CalendarDto populateCalendarVars(Integer year);
    PostDtoView populateMyVars(int userId, int offset, int limit, Post.Status status);
    PostDtoView populateVarsModeration(int offset, int limit, String status);
    ResponseEntity<ResponseApi> makeNewPost(Post post, User user);
    ResponseEntity<ResponseApi> changePost(int id, RequestApi post, User user);
    ResponseEntity<ResponseApi> makeNewLike(int postId, User user);
    ResponseEntity<ResponseApi> makeNewDisLike(int postId, User user);
    ResponseApi postModeration(RequestApi requestApi, User user);
}
