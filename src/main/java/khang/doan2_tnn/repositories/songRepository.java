package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface songRepository extends JpaRepository<songs, Long> {
    @Query("SELECT s FROM songs s " +
            "WHERE (:name IS NULL OR s.songName LIKE %:name%)"+
            "AND (:artist IS NULL OR s.artist LIKE %:artist%)" +
            "AND (:genre IS NULL OR s.genre = :genre)")
    List<songs> findByNameorArtistorGenre(String name, String artist, String genre);
}
