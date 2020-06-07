package main.repositories;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_votes ON posts.id=post_votes.post_id AND post_votes.value = 1" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time < current_time" +
            " GROUP BY posts.id ORDER BY COUNT(post_votes.value) DESC", nativeQuery = true)
    List<Post> findPostByLikeCount (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND (text LIKE %:query% OR title LIKE %:query%)", nativeQuery = true)
    List<Post> findPostBySearchQuery (Pageable pageable, @Param("query") String query);
    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time", nativeQuery = true)
    Long countPost();
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time LIKE %:date%", nativeQuery = true)
    List<Post> findPostWithExactDate (Pageable pageable, @Param("date") String date);
    @Query(value = "SELECT * FROM posts JOIN tag2post ON posts.id=tag2post.post_id JOIN tags ON tags.id=tag2post.tag_id " +
            "WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time < current_time AND tags.name = :tag",nativeQuery = true)
    List<Post> findPostsByTag (Pageable pageable, @Param("tag") String tag);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND id = :id", nativeQuery = true)
    Post getPostById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND (text LIKE %:query% OR title LIKE %:query%)", nativeQuery = true)
    Long countPostWithSearchQuery(@Param("query") String query);
    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time LIKE %:date%)", nativeQuery = true)
    Long countPostWithExactDate(@Param("date") String date);
    @Query(value = "SELECT COUNT(*) FROM posts JOIN tag2post ON posts.id=tag2post.post_id JOIN tags ON tags.id=tag2post.tag_id" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time < current_time AND tags.name = :tag", nativeQuery = true)
    Long countPostWithTag(@Param("tag") String tag);
    @Modifying
    @Query(value = "UPDATE posts SET view_count = view_count + 1 WHERE id = :id", nativeQuery = true)
    void updateViewCount(@Param("id") int id);
    @Query(value = "SELECT CONCAT(YEAR(time), '-', MONTH(time), '-', DAY(time)) AS date, COUNT(*) FROM posts WHERE YEAR(time) = :year GROUP BY time ORDER BY COUNT(*) DESC",nativeQuery = true)
    List<List> getPostsByYears (@Param("year") int year);
    @Query(value = "SELECT YEAR(time) FROM posts GROUP BY YEAR(time) ORDER BY YEAR(time) DESC",nativeQuery = true)
    List<Integer> getPostsAllYears ();
    @Query(value = "SELECT COUNT(*) FROM posts WHERE moderation_status = 'NEW'",nativeQuery = true)
    Long countNewPostsToModerator ();
    @Query(value = "SELECT * FROM posts WHERE is_active = 0" +
            " AND time < current_time AND user_id = :id ORDER BY time", nativeQuery = true)
    List<Post> findInactivePosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'NEW'" +
            " AND time < current_time AND user_id = :id ORDER BY time", nativeQuery = true)
    List<Post> findPendingPosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'DECLINED'" +
            " AND time < current_time AND user_id = :id ORDER BY time", nativeQuery = true)
    List<Post> findDeclinedPosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND time < current_time AND user_id = :id ORDER BY time", nativeQuery = true)
    List<Post> findAcceptedPosts (Pageable pageable, @Param("id") int id);
}
