package main.repositories;

import main.model.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagToPostRepository extends JpaRepository<TagToPost, Integer> {
}
