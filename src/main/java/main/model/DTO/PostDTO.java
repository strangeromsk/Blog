package main.model.DTO;

import lombok.Data;
import main.model.Post;
import main.model.User;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
public class PostDTO {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Post post;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @Column(name = "id",insertable = false, updatable = false)
    private Integer id;
    private Date time = post.getTime();
    @Column(name = "user_id",insertable = false, updatable = false)
    private Integer userId;
    private String name = user.getName();
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
