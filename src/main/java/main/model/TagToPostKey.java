package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TagToPostKey implements Serializable {
    @Column(name = "post_id")
    Integer postId;
    @Column(name = "tag_id")
    Integer tagId;
}
