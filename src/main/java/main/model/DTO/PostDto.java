package main.model.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class PostDto {

    private Integer id;
    private Date time;
    private Integer userId;
    private String name;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
