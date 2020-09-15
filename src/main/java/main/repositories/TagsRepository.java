package main.repositories;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT * FROM tags WHERE name LIKE :query%", nativeQuery = true)
    List<Tag> findTagsByQuery (@Param("query") String query);

    List<Tag> findTagsByNameIn(List<String> name);

    @Query(value = "SELECT DISTINCT tags.* FROM tags " +
            "LEFT JOIN tag2post " +
            "ON tags.id = tag2post.tag_id " +
            "LEFT JOIN posts " +
            "ON tag2post.post_id = posts.id " +
            "WHERE is_active = 1 " +
            "AND moderation_status = 'ACCEPTED' " +
            "AND posts.timestamp <= NOW() + INTERVAL 3 HOUR", nativeQuery = true)
    List<Tag> findTagsOnActivePosts();
}
