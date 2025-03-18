package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface userRepository extends JpaRepository<users, Long> {
    users findByUsername(String username);

    users findByEmail(String email);

    users findByUserId(String userId);

    List<users> findByRole(String role);

    void deleteByUserId(String userId);

    @Query("SELECT u FROM users u " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE %:fullName%)"+
            "AND (:email IS NULL OR u.email = :email)")
    List<users> searchUsers(String fullName, String email);
}
