package main.services;

import main.DTO.PostDTOById.CommentDtoById;
import main.mapper.CommentMapper;
import main.repositories.PostCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {
    private final CommentMapper commentMapper;
    private final PostCommentsRepository postCommentsRepository;

    @Autowired
    public PostCommentService(CommentMapper commentMapper, PostCommentsRepository postCommentsRepository) {
        this.commentMapper = commentMapper;
        this.postCommentsRepository = postCommentsRepository;
    }

    public CommentDtoById getCommentPostById(int id){
        return commentMapper.toDto(postCommentsRepository.getPostCommentById(id));
    }
}
