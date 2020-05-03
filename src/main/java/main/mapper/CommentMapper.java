package main.mapper;

import main.DTO.PostDTOById.CommentDtoById;
import main.model.PostComment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class CommentMapper {
    @Autowired
    private ModelMapper modelMapper;

    public PostComment toEntity(CommentDtoById commentDtoById){
        return Objects.isNull(commentDtoById) ? null : modelMapper.map(commentDtoById, PostComment.class);
    }

    public CommentDtoById toDto(PostComment postComment){
        return Objects.isNull(postComment) ? null : modelMapper.map(postComment, CommentDtoById.class);
    }
}
