package main.model.repositories;

import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time > current_time ORDER BY time LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Post> findPostByDateAsc (@Param("offset") int offset, @Param("limit") int limit);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time > current_time ORDER BY time DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Post> findPostByDateDesc (@Param("offset") int offset, @Param("limit") int limit);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time > current_time LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Post> findPost (@Param("offset") int offset, @Param("limit") int limit);
}
