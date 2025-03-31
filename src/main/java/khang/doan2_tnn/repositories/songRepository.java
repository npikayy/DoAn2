package khang.doan2_tnn.repositories;

import jakarta.transaction.Transactional;
import khang.doan2_tnn.entities.songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface songRepository extends JpaRepository<songs, Long> {
    @Query("SELECT s FROM songs s " +
            "WHERE (:name IS NULL OR s.songName LIKE %:name%)"+
            "AND (:artist IS NULL OR s.artist LIKE %:artist%)" +
            "AND (:genre IS NULL OR s.genre = :genre)")
    List<songs> findByNameorArtistorGenre(String name, String artist, String genre);
    @Query("SELECT s FROM songs s WHERE s.artist LIKE %:artist%")
    List<songs> findByArtist(String artist);
    @Query("SELECT s FROM songs s WHERE s.genre = :genre")
    List<songs> findByGenre(String genre);
    @Query("SELECT s FROM songs s WHERE s.songName like %:songName%")
    List<songs> findBySongName(String songName);
    @Query("SELECT s FROM songs s WHERE s.songId NOT IN :ids")
    List<songs> findSongIdNotIn(List<Long> ids);
    @Query("SELECT s FROM songs s WHERE " +
            "(LOWER(s.songName) LIKE %:name% OR :name IS NULL) AND " +
            "(LOWER(s.artist) LIKE %:artist% OR :artist IS NULL) AND " +
            "(LOWER(s.genre) LIKE %:genre% OR :genre IS NULL) AND " +
            "s.songId NOT IN :ids")
    List<songs> findByNameOrArtistOrGenreAndNotInPlaylist(String name, String artist, String genre, List<Long> ids
    );
    @Transactional
    @Query("UPDATE songs SET songName = :name, artist = :artist, album = :album, genre = :genre, releaseDate = :releaseDate, lastModifiedBy = :lastModifiedBy WHERE songId = :id")
    @Modifying
    void updateSong(Long id, String name, String artist, String album, String genre, LocalDate releaseDate, String lastModifiedBy);

    @Transactional
    @Query("UPDATE songs SET songPicUrl = :songPicUrl, lastModifiedBy = :lastModifiedBy WHERE songId = :id")
    @Modifying
    void updatePicUrl(Long id, String songPicUrl, String lastModifiedBy);

    @Transactional
    @Query("UPDATE songs SET SongUrl = :songUrl, duration = :duration, lastModifiedBy = :lastModifiedBy WHERE songId = :id")
    @Modifying
    void updateSongUrl(Long id, String songUrl, String duration, String lastModifiedBy);
}
