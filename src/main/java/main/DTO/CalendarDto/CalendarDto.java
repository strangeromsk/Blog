package main.DTO.CalendarDto;

import lombok.Data;
import main.model.Post;

import java.util.List;

@Data
public class CalendarDto {
    List<Integer> listYears;
    List<Post> posts;
}
