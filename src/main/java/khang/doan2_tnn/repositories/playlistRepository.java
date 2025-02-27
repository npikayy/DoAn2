package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.playlists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface playlistRepository extends JpaRepository<playlists, Long> {
}
