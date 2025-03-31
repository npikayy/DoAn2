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
                picDir.mkdirs(); // Create directory if it doesn't exist
            }

            // Original file name
            String originalName = imageFile.getOriginalFilename();
            String picName = originalName;
            String fileExtension = "";

            // Extract file extension if present
            int lastDotIndex = originalName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                fileExtension = originalName.substring(lastDotIndex); // Get file extension
                picName = originalName.substring(0, lastDotIndex); // File name without extension
            }

            File picFile = new File(PIC_DIR + originalName);
            int counter = 1; // Start with 1 for duplicate files

            // Generate a unique name by appending a number if the file exists
            while (picFile.exists()) {
                picFile = new File(PIC_DIR + picName + "_" + counter + fileExtension);
                counter++;
            }

            // Save the file
            try (FileOutputStream fos = new FileOutputStream(picFile)) {
                fos.write(imageFile.getBytes());
            }
        artists artist = artists.builder()
                .artistName(artistName)
                .artistBio(artistBio)
                .artistPicUrl("/artistPics/" + picFile.getName())
                .build();

        artistRepository.save(artist);
        } catch (IOException e) {
        }
    }
    public void updateArtist(Long artistId, String artistName, String artistBio) throws IOException {
        artists artist = artistRepository.findById(artistId).orElse(null);
        if (artist != null) {
                artistRepository.updateArtist(artistName, artistBio, artistId);
        }
    }
    public void updatePicUrl(Long artistId, MultipartFile imageFile) throws IOException {
        // Fetch artist from the database
        artists artist = artistRepository.findById(artistId).orElseThrow(
                () -> new IllegalArgumentException("Artist not found with ID: " + artistId)
        );

        // Original file name
        String originalName = imageFile.getOriginalFilename();
        if (originalName == null || originalName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        String picName = originalName;
        String fileExtension = "";

        // Extract file extension if present
        int lastDotIndex = originalName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            fileExtension = originalName.substring(lastDotIndex); // Get file extension
            picName = originalName.substring(0, lastDotIndex);    // File name without extension
        }

        // Check for duplicate file names and generate a unique name
        File picFile = new File(PIC_DIR + originalName);
        int counter = 1;
        while (picFile.exists()) {
            picFile = new File(PIC_DIR + picName + "_" + counter + fileExtension);
            counter++;
        }

        // Save the new file
        try (FileOutputStream fos = new FileOutputStream(picFile)) {
            fos.write(imageFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }

        // Delete old file if it exists
        String oldPicUrl = artist.getArtistPicUrl();
        if (oldPicUrl != null) {
            String oldPicPath = oldPicUrl.replace("/artistPics/", "src/main/resources/static/artistPics/");
            File oldPicFile = new File(oldPicPath);
            if (oldPicFile.exists() && !oldPicFile.delete()) {
                System.err.println("Failed to delete old file: " + oldPicPath);
            }
        }

        // Update the artist's picture URL
        String newPicUrl = "/artistPics/" + picFile.getName();
        artistRepository.updateArtistPicUrl(newPicUrl, artistId);
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
