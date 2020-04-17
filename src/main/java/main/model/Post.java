package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "posts")
public class Post {

    public enum Status{
        NEW, ACCEPTED, DECLINED
    }
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
}
