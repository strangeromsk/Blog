package main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter

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
}
