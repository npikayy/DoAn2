package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.favoriteSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface favoriteSongRepository extends JpaRepository<favoriteSongs, Long> {
    @Transactional
    @Modifying
    @Query("delete from favoriteSongs fs WHERE fs.userId =?1 AND fs.songId =?2")
    void deleteByUserIdAndSongId(String userId, Long songId);
    @Query("SELECT fs.songId FROM favoriteSongs fs WHERE fs.userId =?1")
    List<Long> findSongIdByUserId(String userId);
    @Query("SELECT fs FROM favoriteSongs fs WHERE fs.userId =?1 AND fs.songId =?2")
    favoriteSongs findByUserIdAndSongId(String userId, Long songId);
}
