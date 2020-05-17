package main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class TagToPostKey implements Serializable {
    @Column(name = "post_id")
    Integer postId;
    @Column(name = "tag_id")
    Integer tagId;
}
