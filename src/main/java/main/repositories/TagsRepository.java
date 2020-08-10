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
}
