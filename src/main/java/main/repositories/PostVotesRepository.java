package main.repositories;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVote, Long> {
    @Query(value = "SELECT * FROM post_votes WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
    Optional<PostVote> getLikeByUserAndPost(@Param("postId") int postId, @Param("userId") int userId);
}
