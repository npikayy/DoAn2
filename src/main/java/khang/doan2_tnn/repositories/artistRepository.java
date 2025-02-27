package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.artists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface artistRepository extends JpaRepository<artists, Long> {
}
