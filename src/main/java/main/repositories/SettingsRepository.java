package main.repositories;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingsRepository extends JpaRepository<GlobalSettings, Integer> {
    @Modifying
    @Query(value = "UPDATE global_settings SET value = :value WHERE name = 'MULTIUSER_MODE'", nativeQuery = true)
    void updateMultiuserMode(@Param("value") boolean value);
    @Modifying
    @Query(value = "UPDATE global_settings SET value = :value WHERE name = 'POST_PREMODERATION'", nativeQuery = true)
    void updatePostPremoderation(@Param("value") boolean value);
    @Modifying
    @Query(value = "UPDATE global_settings SET value = :value WHERE name = 'STATISTICS_IS_PUBLIC'", nativeQuery = true)
    void updateStatistics(@Param("value") boolean value);
    @Query(value = "SELECT value FROM global_settings WHERE code = 'STATISTICS_IS_PUBLIC'", nativeQuery = true)
    boolean getStatIsPublic();
}
