package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "tag2post")
public class TagToPost {

//    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private Set<Post> posts;
//
//    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private Set<Tag> tags;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
