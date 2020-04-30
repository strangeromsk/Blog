package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data

@Entity
@Table(name = "tags")
public class Tag {

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "tag_id", nullable = false, insertable = false, updatable = false)
    private TagToPost tagToPost;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;
}
