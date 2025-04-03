package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.favoriteSongs;
import khang.doan2_tnn.repositories.favoriteSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class favoriteSongService {
    @Autowired
    private favoriteSongRepository favoriteSongRepository;

    public void addFavoriteSong(String userId, Long songId) {
        favoriteSongs favoriteSong = favoriteSongs.builder()
                .userId(userId)
                .songId(songId)
                .build();
        favoriteSongRepository.save(favoriteSong);
    }
    public void removeFavoriteSong(String userId, Long songId) {
        favoriteSongRepository.deleteByUserIdAndSongId(userId,songId);
    }
}
