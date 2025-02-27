package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.playlistSongs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface playlistsongRepository extends JpaRepository<playlistSongs, Long> {
}
