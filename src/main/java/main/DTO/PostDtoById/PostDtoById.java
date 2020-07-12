package main.DTO.PostDtoById;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PostDtoById {
    private long id;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
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
