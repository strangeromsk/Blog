package main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "posts")
public class Post {

    public enum Status{
        NEW, ACCEPTED, DECLINED
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostComment> postComments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PostVote> postVotes;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "post_id", nullable = false, insertable = false, updatable = false)
    private TagToPost tagToPost;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "TagToPost",
//            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
//    private Set<Tag> tags = new HashSet<>();

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

//    @NotNull
//    @Column(name = "user_id")
//    private int userId;

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
