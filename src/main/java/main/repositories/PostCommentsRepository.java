package main.repositories;

import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    @Query(value = "SELECT * FROM post_comments WHERE id = :id", nativeQuery = true)
    PostComment getPostCommentById(@Param("id") int id);
}
