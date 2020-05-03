package main.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PostDto {

    private Integer id;
    private Date time;

    private UserDto user = new UserDto();

    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
