package main.DTO.PostDtoById;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class CommentDtoById {
    private int id;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
    private Date time;
    private String text;
    private UserDtoById user = new UserDtoById();
}
