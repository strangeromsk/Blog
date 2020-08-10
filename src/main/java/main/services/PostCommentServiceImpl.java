package main.services;

import main.API.ResponseApi;
import main.DTO.PostDtoById.CommentDtoById;
import main.mapper.CommentMapper;
import main.model.Post;
import main.model.PostComment;
import main.repositories.PostCommentsRepository;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import main.services.interfaces.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PostCommentServiceImpl implements PostCommentService {
    private final CommentMapper commentMapper;
    private final PostCommentsRepository postCommentsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostCommentServiceImpl(CommentMapper commentMapper, PostCommentsRepository postCommentsRepository,
                                  PostRepository postRepository, UserRepository userRepository) {
        this.commentMapper = commentMapper;
        this.postCommentsRepository = postCommentsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentDtoById mapCommentPostById(int id){
        return commentMapper.toDto(postCommentsRepository.getOne(id));
    }

    public ResponseEntity<ResponseApi> makeNewComment(int userId, Integer parentId, Integer postId, String text){
        int minTextLength = 10;
        Map<String, String> errors = new HashMap<>(4);
        Optional<Post> post = Optional.ofNullable(postRepository.getPostById(postId));
        if(post.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(text.length() < minTextLength){
            errors.put("text", "Text of comment is too short or does not exist");
        }
        if(errors.size() == 0){
            PostComment postComment = new PostComment();
            postComment.setUser(userRepository.getOne(userId));
            postComment.setTimestamp(new Date().getTime());
            postComment.setText(text);
            if(parentId != null){
                postComment.setParentId(parentId);
            }
            postComment.setPost(post.get());
            postCommentsRepository.save(postComment);
            return new ResponseEntity<>(ResponseApi.builder().id(postComment.getId()).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseApi.builder().result(false).errors(errors).build(), HttpStatus.BAD_REQUEST);
    }
}
