package main.DTO.moderation;

import lombok.Data;
import main.DTO.UserDto;

import java.util.Date;
@Data
public class PostDtoModeration {
    private Integer id;
    private Date time;

    private UserDto user = new UserDto();

    private String title;
    private String announce;
}
