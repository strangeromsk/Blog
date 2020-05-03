package main.mapper;

import main.DTO.PostDTOById.PostDtoById;
import main.DTO.PostDto;
import main.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PostMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Post toEntity(PostDto postDto){
        return Objects.isNull(postDto) ? null : modelMapper.map(postDto, Post.class);
    }

    public PostDto toDto(Post post){
        return Objects.isNull(post) ? null : modelMapper.map(post, PostDto.class);
    }

    public PostDtoById toDtoById(Post post){
        return Objects.isNull(post) ? null : modelMapper.map(post, PostDtoById.class);
    }
}
