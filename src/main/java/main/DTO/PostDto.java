package main.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import java.util.Date;

@Data
public class PostDto {

    private Integer id;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-M-yyyy hh:mm:ss")
    private Date time;
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static void disableMapper(){
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private UserDto user = new UserDto();

    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
