package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<users, Long> {
    users findByUsername(String username);
}
