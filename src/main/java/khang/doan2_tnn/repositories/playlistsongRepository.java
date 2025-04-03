package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.playlistSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface playlistsongRepository extends JpaRepository<playlistSongs, Long> {
    @Query("SELECT ps.songId FROM playlistSongs ps WHERE ps.playlistId = :playlistId")
    List<Long> findSongIdByPlaylistId(String playlistId);
    @Transactional
    @Modifying
    @Query("DELETE FROM playlistSongs ps WHERE ps.playlistId = :playlistId AND ps.songId = :songId")
    void deleteById(String playlistId, long songId);

    @Query("SELECT ps.playlistId FROM playlistSongs ps WHERE ps.songId = :songId")
    List<String> findPlaylistIDBySongId(Long songId);

    @Query("SELECT ps FROM playlistSongs ps WHERE ps.songId = :songId AND ps.playlistId = :playlistId")
    playlistSongs findByPlaylistIdAndSongId(String playlistId, Long songId);

    @Transactional
    @Modifying
    @Query("DELETE FROM playlistSongs ps WHERE ps.playlistId = :playlistId AND ps.songId = :songId")
    void deleteByPlaylistIdAndSongId(String playlistId, long songId);
}
