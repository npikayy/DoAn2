package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.artists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface artistRepository extends JpaRepository<artists, Long> {
    @Transactional
    @Query("UPDATE artists SET artistName =?1, artistBio =?2 WHERE artistId =?3")
    @Modifying
    void updateArtist(String artistName, String artistBio, Long artistId);

    @Transactional
    @Query("UPDATE artists SET artistPicUrl =?1 WHERE artistId =?2")
    @Modifying
    void updateArtistPicUrl(String artistPicUrl, Long artistId);
}
