package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.repositories.songRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class songService {
    static final String MUSIC_DIR = "src/main/resources/static/music/";
    static final String PIC_DIR = "src/main/resources/static/music/pic/";

    @Autowired
    private songRepository songRepository;
    public List<songs> findAll() {
        return songRepository.findAll();
    }
    public List<songs> findByNameorArtistorGenre(String name, String artist, String genre) {
        return songRepository.findByNameorArtistorGenre(name, artist, genre);
    }
    public songs findById(Long id) {
        return songRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        songs song = songRepository.findById(id).orElse(null);
        if (song != null) {
            String songUrl = song.getSongUrl();
            String newSongUrl = songUrl.replace("/music/", "src/main/resources/static/music/");
            File oldFile = new File(newSongUrl);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            String picUrl = song.getSongPicUrl();
            String newPicUrl = picUrl.replace("/music/pic/", "src/main/resources/static/music/pic/");
            File oldPicFile = new File(newPicUrl);
            if (oldPicFile.exists()) {
                oldPicFile.delete();
            }
            songRepository.deleteById(id);
        }
    }

    private String formatDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    public void uploadMusic(MultipartFile file, MultipartFile file2, String name, String artist, String album, String genre, LocalDate releaseDate) {

        try {
            // Tạo thư mục lưu trữ nếu chưa tồn tại
            File musicDir = new File(MUSIC_DIR);
            if (!musicDir.exists()) {
                musicDir.mkdirs();
            }
            File picDir = new File(PIC_DIR);
            if (!picDir.exists()) {
                picDir.mkdirs();
            }
            // Lưu ảnh bìa
            String picName = file2.getOriginalFilename();
            File picFile = new File(PIC_DIR + picName);
            try (FileOutputStream fos = new FileOutputStream(picFile)) {
                fos.write(file2.getBytes());
            }

            // Lưu tệp vào thư mục
            String fileName = file.getOriginalFilename();
            File musicFile = new File(MUSIC_DIR + fileName);
            try (FileOutputStream fos = new FileOutputStream(musicFile)) {
                fos.write(file.getBytes());
            }
            String musicFilePath = "src/main/resources/static/music/" + file.getOriginalFilename();
            long durationInSeconds = AudioUtils.getAudioDurationInSeconds(musicFilePath);
            String duration = formatDuration(durationInSeconds);

            // Tạo dòng mới trong bảng songs
            songs song = songs.builder()
                    .songName(name)
                    .artist(artist)
                    .album(album)
                    .genre(genre)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .SongUrl("/music/" + file.getOriginalFilename())
                    .songPicUrl("/music/pic/" + file2.getOriginalFilename())
                    .build();

            songRepository.save(song);

        } catch (IOException e) {
        }
    }
    public void updateMusic(MultipartFile file1, MultipartFile file2, Long id, String name, String artist, String album, String genre, LocalDate releaseDate) throws IOException {
        songs song = songRepository.findById(id).orElse(null);
        String newSongUrl = "/music/" + file1.getOriginalFilename();
        String newPicUrl = "/music/pic/" + file2.getOriginalFilename();
        if (!newSongUrl.equals(song.getSongUrl())) {
            String songUrl = song.getSongUrl();
            newSongUrl = songUrl.replace("/music/", "src/main/resources/static/music/");
            File oldFile = new File(newSongUrl);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
        if (!newPicUrl.equals(song.getSongPicUrl())) {
            String picUrl = song.getSongPicUrl();
            newPicUrl = picUrl.replace("/music/pic/", "src/main/resources/static/music/pic/");
            File oldFile = new File(newPicUrl);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
        // Lưu ảnh bìa
        File picFile = new File(PIC_DIR + file2.getOriginalFilename());
        FileOutputStream fos1 = new FileOutputStream(picFile);
            fos1.write(file2.getBytes());

        // Lưu tệp vào thư mục
        File musicFile = new File(MUSIC_DIR + file1.getOriginalFilename());
        FileOutputStream fos2 = new FileOutputStream(musicFile);
            fos2.write(file1.getBytes());
        String musicFilePath = "src/main/resources/static/music/" + file1.getOriginalFilename();
        long durationInSeconds = AudioUtils.getAudioDurationInSeconds(musicFilePath);
        String duration = formatDuration(durationInSeconds);

        songs songToUpdate = songs.builder()
                .songId(id)
                .songName(name)
                .artist(artist)
                .album(album)
                .genre(genre)
                .releaseDate(releaseDate)
                .duration(duration)
                .SongUrl("/music/" + file1.getOriginalFilename())
                .songPicUrl("/music/pic/" + file2.getOriginalFilename())
                .build();
        songRepository.save(songToUpdate);
    }
}
