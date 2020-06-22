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
    @EmbeddedId
    TagToPostKey id;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @MapsId("tag_id")
    @JoinColumn(name = "tag_id")
    Tag tag;
}
