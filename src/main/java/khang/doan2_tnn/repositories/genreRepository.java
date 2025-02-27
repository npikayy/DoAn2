package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface genreRepository extends JpaRepository<genres, Long> {
}
