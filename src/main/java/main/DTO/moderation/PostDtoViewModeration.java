package main.DTO.moderation;

import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
public class PostDtoViewModeration {
    private long count;
    @ToString.Exclude
    private List<PostDtoModeration> posts;
}
