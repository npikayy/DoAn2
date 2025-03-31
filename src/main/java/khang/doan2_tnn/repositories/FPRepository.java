package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.ForgotPassword;
import khang.doan2_tnn.entities.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FPRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = :otp AND fp.user = :user")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, users user);
    @Transactional
    void deleteByFpId(Integer fpId);

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.user = :user")
    ForgotPassword findByUser(users user);
}
