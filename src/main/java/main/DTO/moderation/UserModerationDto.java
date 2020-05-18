package main.DTO.moderation;

import lombok.Data;

@Data
public class UserModerationDto {
    private Integer id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;
}
