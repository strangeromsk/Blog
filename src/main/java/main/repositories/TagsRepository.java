package main.repositories;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {
    @Query(value = "SELECT tags.id, tags.name FROM tags JOIN tag2post ON tags.id=tag2post.tag_id JOIN posts ON posts.id=tag2post.post_id " +
            "WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time < current_time",nativeQuery = true)
    List<Tag> findTags ();
}
