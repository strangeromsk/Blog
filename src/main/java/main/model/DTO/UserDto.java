package main.model.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserDto {
    private Integer userId;
    private String name;
}
