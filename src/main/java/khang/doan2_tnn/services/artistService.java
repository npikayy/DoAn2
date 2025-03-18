package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.artists;
import khang.doan2_tnn.entities.songs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import khang.doan2_tnn.repositories.artistRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class artistService {
    static final String PIC_DIR = "src/main/resources/static/artistPics/";
    @Autowired
    private artistRepository artistRepository;
    public List<artists> getArtists() {
        return artistRepository.findAll();
    }
    public artists getArtistById(Long artistId) {
        return artistRepository.findById(artistId).orElse(null);
    }
    public void addArtist(String artistName, String artistBio, MultipartFile imageFile) throws FileNotFoundException {
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
        artists artist = artists.builder()
                .artistName(artistName)
                .artistBio(artistBio)
                .artistPicUrl("/artistPics/" + imageFile.getOriginalFilename())
                .build();

        artistRepository.save(artist);
        } catch (IOException e) {
        }
    }
    public void updateArtist(Long artistId, String artistName, String artistBio, MultipartFile imageFile) throws IOException {
        artists artist = artistRepository.findById(artistId).orElse(null);
        if (artist != null) {
            if (imageFile != null) {
                // Xóa ảnh cũ
                String oldPicUrl = artist.getArtistPicUrl();
                String newPicUrl = oldPicUrl.replace("/artistPics/", "src/main/resources/static/artistPics/");
                File oldPicFile = new File(newPicUrl);
                if (oldPicFile.exists()) {
                    oldPicFile.delete();
                }
                // Lưu ảnh mới
                String picName = imageFile.getOriginalFilename();
                File picFile = new File( PIC_DIR + picName);
                try (FileOutputStream fos = new FileOutputStream(picFile)) {
                    fos.write(imageFile.getBytes());
                }
                artist.setArtistName(artistName);
                artist.setArtistBio(artistBio);
                artist.setArtistPicUrl("/artistPics/" + imageFile.getOriginalFilename());
            } else {
                artist.setArtistName(artistName);
                artist.setArtistBio(artistBio);
            }
            artistRepository.save(artist);
        }
    }
    public void deleteArtist(Long artistId) {
        artists artist = artistRepository.findById(artistId).orElse(null);
        if (artist != null) {
            String picUrl = artist.getArtistPicUrl();
            String newPicUrl = picUrl.replace("/artistPics/", "src/main/resources/static/artistPics/");
            File oldPicFile = new File(newPicUrl);
            if (oldPicFile.exists()) {
                oldPicFile.delete();
            }
            artistRepository.deleteById(artistId);
        }
    }
}
