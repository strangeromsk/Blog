package main.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.EnableMBeanExport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data

@Entity
@Table(name = "tag2post")
public class TagToPost implements Serializable {

//    @Id
//    @NotNull
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @EmbeddedId
    TagToPostKey id;

//    @NotNull
//    @Column(name = "post_id")
//    private Integer postId;
//
//    @NotNull
//    @Column(name = "tag_id")
//    private Integer tagId;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @MapsId("tag_id")
    @JoinColumn(name = "tag_id")
    Tag tag;

//    @ManyToMany(mappedBy = "tagToPost")
//    @JoinTable(
//            name = "posts",
//            joinColumns = {@JoinColumn(name = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "post_id")}
//    )
//    private List<Post> posts;

//    @ManyToMany(mappedBy = "tagToPost")
//    @JoinTable(
//            name = "tags",
//            joinColumns = {@JoinColumn(name = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
//    )
//    private List<Tag> tags;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Tag> tags;
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "tagToPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Post> posts;
}
