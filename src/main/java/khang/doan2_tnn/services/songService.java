package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.repositories.playlistsongRepository;
import khang.doan2_tnn.repositories.songRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    @Autowired
    private playlistsongRepository playlistsongRepository;
    @Autowired
    private playlistService playlistService;

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
        List<String> playlistIds = playlistsongRepository.findPlaylistIDBySongId(id);
        for (String playlistId : playlistIds) {
            playlistService.deleteSongFromPlaylist(playlistId, id);
        }
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
    public void uploadMusic(MultipartFile musicFile, MultipartFile coverImageFile, String name, String artist, String album, String genre, LocalDate releaseDate, String addedBy, String lastModifiedBy) {
        try {
            // Ensure directories exist
            File musicDir = new File(MUSIC_DIR);
            if (!musicDir.exists()) {
                musicDir.mkdirs();
            }

            File picDir = new File(PIC_DIR);
            if (!picDir.exists()) {
                picDir.mkdirs();
            }

            // Handle unique naming for cover image
            String originalPicName = coverImageFile.getOriginalFilename();
            if (originalPicName.isEmpty()) {
                throw new IllegalArgumentException("File name for cover image cannot be null or empty");
            }

            String picName = originalPicName;
            String picExtension = "";
            int lastDotIndex = originalPicName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                picExtension = originalPicName.substring(lastDotIndex); // File extension
                picName = originalPicName.substring(0, lastDotIndex);    // Base name
            }

            File picFile = new File(PIC_DIR + originalPicName);
            int counter = 1;
            while (picFile.exists()) {
                picFile = new File(PIC_DIR + picName + "_" + counter + picExtension);
                counter++;
            }

            // Save the cover image file
            try (FileOutputStream fos = new FileOutputStream(picFile)) {
                fos.write(coverImageFile.getBytes());
            }

            // Handle unique naming for music file
            String originalMusicName = musicFile.getOriginalFilename();
            if (originalMusicName.isEmpty()) {
                throw new IllegalArgumentException("File name for music cannot be null or empty");
            }

            String musicName = originalMusicName;
            String musicExtension = "";
            lastDotIndex = originalMusicName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                musicExtension = originalMusicName.substring(lastDotIndex); // File extension
                musicName = originalMusicName.substring(0, lastDotIndex);   // Base name
            }

            File musicFilePath = new File(MUSIC_DIR + originalMusicName);
            counter = 1;
            while (musicFilePath.exists()) {
                musicFilePath = new File(MUSIC_DIR + musicName + "_" + counter + musicExtension);
                counter++;
            }

            // Save the music file
            try (FileOutputStream fos = new FileOutputStream(musicFilePath)) {
                fos.write(musicFile.getBytes());
            }

            // Calculate song duration
            String musicPath = "src/main/resources/static/music/" + musicFilePath.getName();
            long durationInSeconds = AudioUtils.getAudioDurationInSeconds(musicPath);
            String duration = formatDuration(durationInSeconds);

            // Create a new record in the songs table
            songs song = songs.builder()
                    .songName(name)
                    .artist(artist)
                    .album(album)
                    .genre(genre)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .addedBy(addedBy)
                    .lastModifiedBy(lastModifiedBy)
                    .SongUrl("/music/" + musicFilePath.getName())
                    .songPicUrl("/music/pic/" + picFile.getName())
                    .build();

            // Save to the database
            songRepository.save(song);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while uploading music or cover image", e);
        }
    }
    public void updateMusic(Long id,String name, String artist, String album, String genre, LocalDate releaseDate, String lastModifiedBy) throws IOException {
        songs song = songRepository.findById(id).orElse(null);
        if (song != null) {
            songRepository.updateSong(id, name, artist, album, genre, releaseDate, lastModifiedBy);
        }
    }

    public void updatePicFile(MultipartFile coverImage, long id, String lastModifiedBy) throws IOException {
        // Fetch the song from the repository
        songs song = songRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Song not found with ID: " + id)
        );

        // Prepare the new picture name and URL
        String originalPicName = coverImage.getOriginalFilename();
        if (originalPicName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        String picName = originalPicName;
        String picExtension = "";
        int lastDotIndex = originalPicName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            picExtension = originalPicName.substring(lastDotIndex); // File extension
            picName = originalPicName.substring(0, lastDotIndex);   // Base name without extension
        }

        // Generate a unique name if the file already exists
        File picFile = new File(PIC_DIR + originalPicName);
        int counter = 1;
        while (picFile.exists()) {
            picFile = new File(PIC_DIR + picName + "_" + counter + picExtension);
            counter++;
        }

        // Delete the old file if the new name is different
        String newPicUrl = "/music/pic/" + picFile.getName();
        if (!newPicUrl.equals(song.getSongPicUrl())) {
            String oldPicUrl = song.getSongPicUrl();
            String oldPicPath = oldPicUrl.replace("/music/pic/", "src/main/resources/static/music/pic/");
            File oldFile = new File(oldPicPath);
            if (oldFile.exists() && !oldFile.delete()) {
                System.err.println("Failed to delete old file: " + oldPicPath);
            }
        }

        // Save the new file
        try (FileOutputStream fos = new FileOutputStream(picFile)) {
            fos.write(coverImage.getBytes());
        }

        // Update the database with the new picture URL
        songRepository.updatePicUrl(id, newPicUrl, lastModifiedBy);
    }

    public void updateMusicFile(MultipartFile musicFile, long id, String lastModifiedBy) throws IOException {
        // Fetch the song from the repository
        songs song = songRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Song not found with ID: " + id)
        );

        // Prepare the original file name and validate it
        String originalMusicName = musicFile.getOriginalFilename();
        if (originalMusicName == null || originalMusicName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        // Extract base name and extension for unique naming
        String musicName = originalMusicName;
        String musicExtension = "";
        int lastDotIndex = originalMusicName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            musicExtension = originalMusicName.substring(lastDotIndex);  // File extension
            musicName = originalMusicName.substring(0, lastDotIndex);    // Base name without extension
        }

        // Generate a unique name if the file already exists
        File musicFilePath = new File(MUSIC_DIR + originalMusicName);
        int counter = 1;
        while (musicFilePath.exists()) {
            musicFilePath = new File(MUSIC_DIR + musicName + "_" + counter + musicExtension);
            counter++;
        }

        // Delete the old file if the new name is different
        String newSongUrl = "/music/" + musicFilePath.getName();
        if (!newSongUrl.equals(song.getSongUrl())) {
            String oldSongUrl = song.getSongUrl();
            String oldSongPath = oldSongUrl.replace("/music/", "src/main/resources/static/music/");
            File oldFile = new File(oldSongPath);
            if (oldFile.exists() && !oldFile.delete()) {
                System.err.println("Failed to delete old file: " + oldSongPath);
            }
        }

        // Save the new music file
        try (FileOutputStream fos = new FileOutputStream(musicFilePath)) {
            fos.write(musicFile.getBytes());
        }

        // Calculate the duration of the song
        String musicPath = "src/main/resources/static/music/" + musicFilePath.getName();
        long durationInSeconds = AudioUtils.getAudioDurationInSeconds(musicPath);
        String duration = formatDuration(durationInSeconds);

        // Update the database with the new music URL and duration
        songRepository.updateSongUrl(id, newSongUrl, duration, lastModifiedBy);
    }
}
