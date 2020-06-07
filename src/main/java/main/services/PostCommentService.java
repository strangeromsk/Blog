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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public ResponseApi makeNewComment(int userId, int parentId, int postId, String text){
        int minTextLength = 10;
        Map<String, String> errors = new HashMap<>(4);
        PostComment postComment = new PostComment();
        Post post = postRepository.getPostById(postId);
        Integer commentId = postCommentsRepository.getOne(parentId).getId();
        Integer postIdByComment = post.getId();

        if(text.length() < minTextLength){
            errors.put("text", "Text of comment is too short or does not exist");
        }
        if(commentId == null){
            errors.put("comment_id", "Comment id is incorrect");
        }
        if(postIdByComment == null){
            errors.put("parent_id", "Parent id is incorrect");
        }
        if(errors.size() == 0){
            postComment.setUser(userRepository.getOne(userId));
            postComment.setTime(new Date());
            postComment.setText(text);
            postComment.setPost(post);
            postCommentsRepository.save(postComment);
            return new ResponseApi().builder().id(postComment.getId()).build();
        }
        return new ResponseApi().builder().result("false").errors(errors).build();
    }
}
