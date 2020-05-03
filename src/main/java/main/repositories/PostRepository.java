package main.repositories;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
    @Query(value = "FROM Post WHERE isActive = 1 AND status = 'ACCEPTED'" +
            " AND time < current_time ORDER BY size(postComments) DESC")
    List<Post> findPostByCommentCount (Pageable pageable);
    @Query(value = "FROM Post WHERE isActive = 1 AND status = 'ACCEPTED'" +
            " AND time < current_time ORDER BY size(postVotes) DESC")
    List<Post> findPostByLikeCount (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND (text LIKE %:query% OR title LIKE %:query%)", nativeQuery = true)
    List<Post> findPostBySearchQuery (Pageable pageable, @Param("query") String query);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND id = :id", nativeQuery = true)
    Post findPostById (@Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time = :sDate", nativeQuery = true)
    List<Post> findPostWithExactDate (Pageable pageable, @Param("sDate") java.sql.Date sDate);
    @Query(value = "SELECT * FROM posts JOIN tag2post ON posts.id=tag2post.post_id JOIN tags ON tags.id=tag2post.tag_id " +
            "WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time < current_time AND tags.name = :tag",nativeQuery = true)
    List<Post> findPostsByTag (Pageable pageable, @Param("tag") String tag);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'NEW'", nativeQuery = true)
    List<Post> findPostWithModeration (Pageable pageable);
}
