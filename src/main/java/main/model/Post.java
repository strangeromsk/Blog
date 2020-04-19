package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

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
    private boolean isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status")
    private Status status;

    @Column(name = "moderator_id")
    private int moderatorId;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @Basic
    @NotNull
    private java.sql.Timestamp time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Column(name = "view_count")
    private int viewCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

//    public Set<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(Set<Tag> tags) {
//        this.tags = tags;
//    }
}
