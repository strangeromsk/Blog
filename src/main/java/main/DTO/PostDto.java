package main.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;

import java.util.Date;

@Data
public class PostDto {
    private Integer id;
    private long timestamp;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private UserDto user = new UserDto();
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
