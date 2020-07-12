package main.DTO;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class PostDtoView {
    private long count;
    @ToString.Exclude
    private List<PostDto> posts;
}
