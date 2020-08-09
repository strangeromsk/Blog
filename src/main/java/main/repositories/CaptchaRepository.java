package main.repositories;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {
    @Modifying
    @Query(value = "UPDATE captcha_codes SET timestamp = current_timestamp, code = :code, secret_code = :secretCode", nativeQuery = true)
    void updateCodes(@Param("code") String code, @Param("secretCode") String secretCode);
    @Modifying
    @Query(value = "DELETE FROM captcha_codes WHERE timestamp > (NOW() - INTERVAL 60 MINUTE)", nativeQuery = true)
    void deleteOlderThan60Minutes();
    @Query(value = "SELECT code FROM captcha_codes WHERE secret_code = :secretCode", nativeQuery = true)
    Optional<String> getCaptchaBySecretCode(@Param("secretCode") String secretCode);
}
