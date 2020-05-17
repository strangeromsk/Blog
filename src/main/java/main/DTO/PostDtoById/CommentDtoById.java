package main.DTO.PostDtoById;

import lombok.Data;

import java.util.Date;
@Data
public class CommentDtoById {
    private int id;
    private Date time;
    private String text;
    private UserDtoById user = new UserDtoById();
}
