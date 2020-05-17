package main.DTO.PostDtoById;

import lombok.Data;

import java.util.*;

@Data
public class PostDtoById {
    private long id;
    private Date time;

    private UserDtoById user = new UserDtoById();

    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
    private List<CommentDtoById> comments = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
}
