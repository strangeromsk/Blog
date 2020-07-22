package main.services.interfaces;

import main.API.ResponseApi;
import org.springframework.http.ResponseEntity;

public interface PostCommentService {
    ResponseEntity<ResponseApi> makeNewComment(int userId, Integer parentId, Integer postId, String text);
}
