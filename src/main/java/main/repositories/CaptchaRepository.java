package main.repositories;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {
    @Modifying
    @Query(value = "UPDATE captcha_codes SET time = current_date, code = :code, secret_code = :secretCode", nativeQuery = true)
    void updateCodes(@Param("code") String code, @Param("secretCode") String secretCode);
    @Modifying
    @Query(value = "DELETE FROM captcha_codes WHERE time > (NOW() - INTERVAL 60 MINUTE)", nativeQuery = true)
    void deleteOlderThan60Minutes();
}
