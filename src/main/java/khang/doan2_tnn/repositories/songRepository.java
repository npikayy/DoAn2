package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.songs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface songRepository extends JpaRepository<songs, Long> {
}
