package main.services;

import main.DTO.PostDtoById.CommentDtoById;
import main.API.ResponseApi;
import main.mapper.CommentMapper;
import main.model.Post;
import main.model.PostComment;
import main.repositories.PostCommentsRepository;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostCommentService {
    private final CommentMapper commentMapper;
    private final PostCommentsRepository postCommentsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostCommentService(CommentMapper commentMapper, PostCommentsRepository postCommentsRepository,
                              PostRepository postRepository, UserRepository userRepository) {
        this.commentMapper = commentMapper;
        this.postCommentsRepository = postCommentsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentDtoById mapCommentPostById(int id){
        return commentMapper.toDto(postCommentsRepository.getOne(id));
    }

    public ResponseApi makeNewComment(int userId, Integer parentId, Integer postId, String text){
        int minTextLength = 10;
        Map<String, String> errors = new HashMap<>(4);
        Optional<Post> post = Optional.ofNullable(postRepository.getPostById(postId));
        //Optional<List<PostComment>> comments = Optional.ofNullable(post.getPostComments());
        if(parentId == null){
            errors.put("parent_id", "parent_id is incorrect");
        }
        if(post.isEmpty()){
            errors.put("Post_id", "Post id is incorrect");
        }
        if(text.length() < minTextLength){
            errors.put("text", "Text of comment is too short or does not exist");
        }
        if(errors.size() == 0 && post.isPresent()){
            PostComment postComment = new PostComment();
            postComment.setUser(userRepository.getOne(userId));
            postComment.setTime(new Date());
            postComment.setText(text);
            postComment.setPost(post.get());
            postCommentsRepository.save(postComment);
            return ResponseApi.builder().id(postComment.getId()).build();
        }
        return ResponseApi.builder().result("false").errors(errors).build();
    }
}
