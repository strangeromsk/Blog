package main.model.DTO;

import lombok.Data;
import main.model.Post;
import main.model.User;;
import java.util.Date;

@Data
public class PostDto {
    private Post post;
    private User user;

    private Integer id = post.getId();
    private Date time = post.getTime();
    private Integer userId = user.getId();
    private String name = user.getName();
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
