package main.model.repositories;

import main.model.DTO.PostDTO;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<PostDTO,Integer> {
}
