package main.DTO;

import lombok.Data;
@Data
public class StatResponse {
    private int postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    private long firstPublication;
}
