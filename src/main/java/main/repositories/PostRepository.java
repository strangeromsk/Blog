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

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PagingAndSortingRepository<Post, Integer> {
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY timestamp", nativeQuery = true)
    List<Post> findPostByDateAsc (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY timestamp DESC", nativeQuery = true)
    List<Post> findPostByDateDesc (Pageable pageable);
    @Query(value = "FROM Post WHERE isActive = 1 AND status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY size(postComments) DESC")
    List<Post> findPostByCommentCount (Pageable pageable);
    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_votes ON posts.id=post_votes.post_id AND post_votes.value = 1" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.timestamp < UNIX_TIMESTAMP()" +
            " GROUP BY posts.id ORDER BY COUNT(post_votes.value) DESC", nativeQuery = true)
    List<Post> findPostByLikeCount (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() AND (text LIKE %:query% OR title LIKE %:query%)", nativeQuery = true)
    List<Post> findPostBySearchQuery (Pageable pageable, @Param("query") String query);
    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP()", nativeQuery = true)
    Long countPost();
    @Query(value = "SELECT COUNT(*) FROM posts WHERE user_id = :id", nativeQuery = true)
    Long countMyPosts(@Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND YEAR(from_unixtime(timestamp)) = :year AND MONTH(from_unixtime(timestamp)) = :month AND DAY(from_unixtime(timestamp)) = :day", nativeQuery = true)
    List<Post> findPostWithExactDate (Pageable pageable, @Param("year") String year, @Param("month") String month, @Param("day") String day);
    @Query(value = "SELECT * FROM posts JOIN tag2post ON posts.id=tag2post.post_id JOIN tags ON tags.id=tag2post.tag_id " +
            "WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.timestamp < UNIX_TIMESTAMP() AND tags.name = :tag", nativeQuery = true)
    List<Post> findPostsByTag (Pageable pageable, @Param("tag") String tag);
    @Query(value = "SELECT * FROM posts WHERE id =:id", nativeQuery = true)
    Post getPostById(@Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() AND id =:id", nativeQuery = true)
    Post getPostByIdApproved(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() AND (text LIKE %:query% OR title LIKE %:query%)", nativeQuery = true)
    Long countPostWithSearchQuery(@Param("query") String query);
    @Query(value = "SELECT COUNT(*) FROM posts JOIN tag2post ON posts.id=tag2post.post_id JOIN tags ON tags.id=tag2post.tag_id" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.timestamp < UNIX_TIMESTAMP() AND tags.name = :tag", nativeQuery = true)
    Long countPostWithTag(@Param("tag") String tag);
    @Modifying
    @Query(value = "UPDATE posts SET view_count = view_count + 1 WHERE id = :id", nativeQuery = true)
    void updateViewCount(@Param("id") int id);
    @Query(value = "SELECT DATE_FORMAT(from_unixtime(`timestamp`),'%Y-%m-%d') AS date, COUNT(*) FROM posts WHERE YEAR(from_unixtime(timestamp)) = :year AND is_active = 1" +
            " AND moderation_status = 'ACCEPTED' GROUP BY timestamp ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<List> getPostsByYears (@Param("year") int year);
    @Query(value = "SELECT DISTINCT YEAR(from_unixtime(timestamp)) FROM posts WHERE is_active = 1" +
            " AND moderation_status = 'ACCEPTED'", nativeQuery = true)
    List<String> getAllYears();
    @Query(value = "SELECT COUNT(*) FROM posts WHERE moderation_status = 'NEW'",nativeQuery = true)
    Long countNewPostsToModerator ();
    @Query(value = "SELECT * FROM posts WHERE is_active = 0" +
            " AND timestamp < UNIX_TIMESTAMP() AND user_id = :id ORDER BY timestamp", nativeQuery = true)
    List<Post> findInactivePosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'NEW'" +
            " AND timestamp < UNIX_TIMESTAMP() AND user_id = :id ORDER BY timestamp", nativeQuery = true)
    List<Post> findPendingPosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'DECLINED'" +
            " AND timestamp < UNIX_TIMESTAMP() AND user_id = :id ORDER BY timestamp", nativeQuery = true)
    List<Post> findDeclinedPosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() AND user_id = :id ORDER BY timestamp", nativeQuery = true)
    List<Post> findAcceptedPosts (Pageable pageable, @Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE user_id = :id", nativeQuery = true)
    List<Post> findAllPostsByUserId (@Param("id") int id);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'NEW'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY timestamp", nativeQuery = true)
    List<Post> findPendingPostsModeration (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'DECLINED'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY timestamp", nativeQuery = true)
    List<Post> findDeclinedPostsModeration (Pageable pageable);
    @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED'" +
            " AND timestamp < UNIX_TIMESTAMP() ORDER BY timestamp", nativeQuery = true)
    List<Post> findAcceptedPostsModeration (Pageable pageable);
}
