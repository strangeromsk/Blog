package main.repositories;

import main.model.Post;
import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    @Query(value = "SELECT * FROM post_comments WHERE post_id = :id", nativeQuery = true)
    List<PostComment> findPostCommentByPostId (@Param("id") int id);
    @Query(value = "SELECT * FROM post_comments WHERE id = :id", nativeQuery = true)
    PostComment getPostCommentById(@Param("id") int id);
}
