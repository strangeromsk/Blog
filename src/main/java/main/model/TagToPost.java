package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tag2post")
@NoArgsConstructor
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

    public TagToPost(TagToPostKey id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return this.id.getPostId().equals(((TagToPost) o).id.getPostId())
                && this.id.getTagId().equals(((TagToPost) o).id.getTagId());
    }

}
