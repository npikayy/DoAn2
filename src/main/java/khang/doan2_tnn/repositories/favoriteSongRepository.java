package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.favoriteSongs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface favoriteSongRepository extends JpaRepository<favoriteSongs, Long> {
}
