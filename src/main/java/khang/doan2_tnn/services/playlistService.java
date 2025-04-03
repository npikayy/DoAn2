package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.playlistSongs;
import khang.doan2_tnn.entities.playlists;
import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.playlistRepository;
import khang.doan2_tnn.repositories.playlistsongRepository;
import khang.doan2_tnn.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class playlistService {
    static final String PIC_DIR = "src/main/resources/static/playlistPics/";
    @Autowired
    private playlistRepository playlistRepository;
    @Autowired
    private playlistsongRepository playlistsongRepository;
    @Autowired
    private userRepository userRepository;

    public List<playlists> getPlaylists() {
        return playlistRepository.findAll();
    }
    public void addPlaylist(String playlistName, String userId, MultipartFile imageFile) {
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
            String creator = userRepository.findByUserId(userId).getRole();
            // Create playlist entity
            playlists playlist = playlists.builder()
                    .playlistName(playlistName)
                    .userId(userId)
                    .playlistPicUrl("/playlistPics/" + picFile.getName()) // Store final file name
                    .createdAt(LocalDate.now())
                    .createdBy(creator)
                    .totalTracks(0)
                    .build();

            // Save to repository
            playlistRepository.save(playlist);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deletePlaylist(String playlistId) {
        playlists playlist = playlistRepository.findById(playlistId).get();
        String picUrl = playlist.getPlaylistPicUrl();
        String newPicUrl = picUrl.replace("/playlistPics/", "src/main/resources/static/playlistPics/");
        File oldPicFile = new File(newPicUrl);
        if (oldPicFile.exists()) {
            oldPicFile.delete();
        }
        playlistRepository.deleteById(playlistId);
    }
    public void deleteSongFromPlaylist(String playlistId, long songId) {
        playlistsongRepository.deleteById(playlistId, songId);
        playlists playlist = playlistRepository.findById(playlistId).get();
        playlist.setTotalTracks(playlist.getTotalTracks() - 1);
        playlistRepository.save(playlist);
    }
    public void updatePlaylistCoverImage(String playlistId,MultipartFile imageFile) {
        playlists playlist = playlistRepository.findById(playlistId).get();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newPicUrl = "/playlistPics/" + picFile.getName();
        String oldPicUrl = playlist.getPlaylistPicUrl();
        String oldPicPath = oldPicUrl.replace("/playlistPics/", "src/main/resources/static/playlistPics/");
        File oldPicFile = new File(oldPicPath);
        if (oldPicFile.exists()) {
            oldPicFile.delete();
        }

        newPicUrl = "/playlistPics/" + originalName;
        System.out.println("New pic URL: " + newPicUrl);
        playlistRepository.updatePlaylistPicURL(newPicUrl, playlist.getPlaylistId());
    }
    public void updatePlaylistName(String playlistId, String newPlaylistName) {
        playlistRepository.updatePlaylistName(newPlaylistName, playlistId);
    }
    public playlists getPlaylistById(String playlistId) {
        return playlistRepository.findById(playlistId).get();
    }
    public void addSongToPlaylist(String playlistId, String songId) {
        try {
            // Lấy danh sách các bài hát đã có trong playlist
            List<Long> danhSachBaiHat = playlistsongRepository.findSongIdByPlaylistId(playlistId);
            System.out.println("Danh sách bài hát trong playlist: " + danhSachBaiHat);

            Long songIdLong = Long.parseLong(songId);

            // Kiểm tra bài hát đã tồn tại chưa
            if (danhSachBaiHat.contains(songIdLong)) {
                System.out.println("Bài hát đã tồn tại trong playlist.");
                return; // Không thêm bài hát nữa
            }

            // Tạo đối tượng playlistSongs và lưu vào cơ sở dữ liệu
            playlistSongs playlistSong = playlistSongs.builder()
                    .songId(songIdLong)
                    .playlistId(playlistId)
                    .build();
            playlists playlist = playlistRepository.findById(playlistId).get();
            playlist.setTotalTracks(playlist.getTotalTracks() + 1);
            playlistRepository.save(playlist);
            playlistsongRepository.save(playlistSong);
            System.out.println("Thêm bài hát vào playlist thành công!");

        } catch (NumberFormatException e) {
            System.err.println("Định dạng songId không hợp lệ: " + songId);
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi khi thêm bài hát vào playlist: " + e.getMessage());
            e.printStackTrace(); // In lỗi chi tiết để hỗ trợ debug
        }
    }
}
