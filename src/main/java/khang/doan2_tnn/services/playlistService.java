package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.artists;
import khang.doan2_tnn.entities.playlists;
import khang.doan2_tnn.repositories.playlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class playlistService {
    static final String PIC_DIR = "src/main/resources/static/playlistPics/";
    @Autowired
    private playlistRepository playlistRepository;

    public List<playlists> getPlaylists() {
        return playlistRepository.findAll();
    }
    public playlists getPlaylistById(String playlistId) {
        return playlistRepository.findById(Long.valueOf(playlistId)).orElse(null);
    }
    public void addPlaylist(String playlistName, String userId, MultipartFile imageFile) {
        try {
            File picDir = new File(PIC_DIR);
            if (!picDir.exists()) {
                picDir.mkdirs();
            }
            // Lưu ảnh bìa
            String picName = imageFile.getOriginalFilename();
            File picFile = new File( PIC_DIR + picName);
            try (FileOutputStream fos = new FileOutputStream(picFile)) {
                fos.write(imageFile.getBytes());
            }
            playlists playlist = playlists.builder()
                    .playlistName(playlistName)
                    .userId(userId)
                    .playlistPicUrl("/playlistPics/" + imageFile.getOriginalFilename())
                    .createdAt(LocalDate.now())
                    .build();

           playlistRepository.save(playlist);
        } catch (IOException e) {
        }
    }
}
