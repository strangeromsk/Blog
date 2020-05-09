package main.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data

@Entity
@Table(name = "tag2post")
public class TagToPost implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "post_id")
    private int postId;

    @NotNull
    @Column(name = "tag_id")
    private int tagId;

    @ToString.Exclude
    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;

    @ToString.Exclude
    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;
}
