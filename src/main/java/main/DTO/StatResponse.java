package main.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StatResponse {
    private int postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date firstPublication;
}
