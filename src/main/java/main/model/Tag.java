package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data

@Entity
@Table(name = "tags")
public class Tag {

//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "tag_id", nullable = false, insertable = false, updatable = false)
//    private TagToPost tagToPost;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "tag2post",
//            joinColumns = {@JoinColumn(name = "tag_id")},
//            inverseJoinColumns = {@JoinColumn(name = "id")}
//    )
//    private List<TagToPost> tagToPost;
//
    @OneToMany(mappedBy = "tag")
    List<TagToPost> tagToPosts;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;
}
