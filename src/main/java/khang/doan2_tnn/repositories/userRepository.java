package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface userRepository extends JpaRepository<users, Long> {
    users findByUsername(String username);

    users findByEmail(String email);

    users findByUserId(String userId);

    List<users> findByRole(String role);

    void deleteByUserId(String userId);
    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.password = :password WHERE u.email = :email")
    void updatePassword(String email, String password);

    @Query("SELECT u FROM users u " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE %:fullName%) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND u.role = 'ROLE_USER'")
    List<users> searchUsers(String fullName, String email);
    @Query("SELECT u FROM users u " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE %:fullName%) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND u.role = 'ROLE_UPLOADER'")
    List<users> searchUploaders(String fullName, String email);

}
