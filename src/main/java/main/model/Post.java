package main.model;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data

@Entity
@Table(name = "posts")
public class Post {

    public enum Status{
        NEW, ACCEPTED, DECLINED
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComment> postComments;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostVote> postVotes;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "post_id", nullable = false, insertable = false, updatable = false)
//    private TagToPost tagToPost;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "tag2post",
//            joinColumns = {@JoinColumn(name = "post_id")},
//            inverseJoinColumns = {@JoinColumn(name = "id")}
//    )
//    private List<TagToPost> tagToPost;

    @OneToMany(mappedBy = "post")
    List<TagToPost> tagToPosts;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private Date time;

    @NotNull
    private String title;

    @NotNull
    @Column(length = 1000)
    private String text;

    @NotNull
    @Column(name = "view_count")
    private int viewCount;
}
