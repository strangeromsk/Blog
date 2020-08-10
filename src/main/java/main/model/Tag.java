package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor

@Entity
@Table(name = "tags")
public class Tag {
    public Tag (String name){
        this.name = name;
    }
    @OneToMany(mappedBy = "tag")
    List<TagToPost> tagToPosts;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;
}
