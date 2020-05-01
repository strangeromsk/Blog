package main.model.repositories;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time ORDER BY time", nativeQuery = true)
    List<Post> findPostByDateAsc (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time ORDER BY time DESC", nativeQuery = true)
    List<Post> findPostByDateDesc (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time", nativeQuery = true)
    List<Post> findPost (Pageable pageable);
}
