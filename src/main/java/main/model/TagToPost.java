package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data

@Entity
@Table(name = "tag2post")
public class TagToPost implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "post_id")
    private int postId;

    @NotNull
    @Column(name = "tag_id")
    private int tagId;
}
