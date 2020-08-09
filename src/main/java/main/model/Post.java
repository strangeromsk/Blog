package main.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data

@Entity
@Table(name = "posts")
public class Post {

    public enum Status{
        NEW, ACCEPTED, DECLINED, declined, inactive, pending, published
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComment> postComments;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostVote> postVotes;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @Column(name = "tags")
    @OneToMany(mappedBy = "post")
    private List<TagToPost> tagToPosts;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "is_active")
    private int isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status")
    private Status status;

    @Column(name = "moderator_id")
    private int moderatorId;

    @Basic
    @NotNull
    private long timestamp;

    @NotNull
    private String title;

    @NotNull
    @Column(length = 5000)
    private String text;

    @NotNull
    @Column(name = "view_count")
    private int viewCount;
}
