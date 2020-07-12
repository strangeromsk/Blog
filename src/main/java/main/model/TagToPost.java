package main.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data

@Entity
@Table(name = "tag2post")
public class TagToPost implements Serializable {
    @EmbeddedId
    TagToPostKey id;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tag_id")
    @JoinColumn(name = "tag_id")
    Tag tag;
}
