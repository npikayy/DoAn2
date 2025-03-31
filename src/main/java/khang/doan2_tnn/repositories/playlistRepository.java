package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.playlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface playlistRepository extends JpaRepository<playlists, String> {
    @Transactional
    @Query("UPDATE playlists SET playlistName =?1 WHERE playlistId =?2")
    @Modifying
    void updatePlaylistName(String playlistName, String playlistId);

    @Transactional
    @Query("UPDATE playlists SET playlistPicUrl=?1 WHERE playlistId =?2")
    @Modifying
    void updatePlaylistPicURL(String playlistPicUrl,String playlistId);
}
