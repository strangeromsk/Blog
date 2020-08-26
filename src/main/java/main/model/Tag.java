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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getName().equals(obj.toString());
    }
}
