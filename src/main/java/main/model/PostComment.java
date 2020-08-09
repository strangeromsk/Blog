package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data

@Entity
@Table(name = "post_comments")
public class PostComment {

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    @Basic
    @NotNull
    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
    private long timestamp;

    @NotNull
    @Column(length = 1000)
    private String text;
}
