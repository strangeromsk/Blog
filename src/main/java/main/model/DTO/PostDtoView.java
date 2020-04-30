package main.model.DTO;

import lombok.Data;
import lombok.ToString;

import java.util.*;

@Data
public class PostDtoView {
    private long count;
    @ToString.Exclude
    private List<PostDto> posts;
}
