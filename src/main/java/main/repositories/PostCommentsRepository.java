package main.repositories;

import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentsRepository extends JpaRepository<PostComment, Integer> {
}
