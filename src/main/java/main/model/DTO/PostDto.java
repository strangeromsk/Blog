package main.model.DTO;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class PostDto {

    private Integer id;
    private Date time;

    private UserDto userDto = new UserDto();

    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
