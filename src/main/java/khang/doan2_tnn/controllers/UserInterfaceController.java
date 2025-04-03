package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.*;
import khang.doan2_tnn.repositories.*;
import khang.doan2_tnn.services.favoriteSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TrangChu")
public class UserInterfaceController {
    @Autowired
    private songRepository songRepository;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private playlistRepository playlistRepository;
    @Autowired
    private playlistsongRepository playlistsongRepository;
    @Autowired
    private artistRepository artistRepository;
    @Autowired
    private genreRepository genreRepository;
    @Autowired
    private favoriteSongRepository favoriteSongRepository;
    @Autowired
    private favoriteSongService favoriteSongService;

    @GetMapping()
    public ModelAndView mainPage(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userMusicPages/mainMusicPage");
        return modelAndView;
    }
    @GetMapping("/getFavoriteSongs")
    public List<songs> getFavoriteSongs(@RequestParam String userId) {
        List<Long> songIds = favoriteSongRepository.findSongIdByUserId(userId);
        return songRepository.findAllById(songIds); // Trả về danh sách bài hát đã thêm vào thiết lập yêu thích
    }
    @GetMapping("/getFavoriteSongIds")
    public List<Long> getFavoriteSongIds(@RequestParam String userId) {
        return favoriteSongRepository.findSongIdByUserId(userId);
    }
    @GetMapping("/addFavoriteSong")
    public String addFavoriteSong(@RequestParam String userId, @RequestParam Long songId) {
        favoriteSongs favoriteSong = favoriteSongRepository.findByUserIdAndSongId(userId, songId);
        System.out.println(favoriteSong == null);
        if (favoriteSong == null) {
            favoriteSongService.addFavoriteSong(userId, songId);
        } else {
            return "0";
        }
        return "1";
    }
    @GetMapping("/removeFavoriteSong")
    public void removeFavoriteSong(@RequestParam String userId, @RequestParam Long songId) {
        favoriteSongService.removeFavoriteSong(userId, songId);
    }
    @GetMapping("/getAllSongs")
    public List<songs> getAllSongs() {
        return songRepository.findAll(); // Trả về danh sách bài hát
    }
    @GetMapping("/searchSongs")
    public List<songs> searchSongs(@RequestParam String songName) {
        return songRepository.findBySongName(songName); // Trả về danh sách bài hát theo tên
    }
    @GetMapping("/getAllArtists")
    public List<artists> getAllArtists() {
        return artistRepository.findAll(); // Trả về danh sách ca sĩ
        }
    @GetMapping("/getArtistSongs")
    public List<songs> getArtistSongs(@RequestParam String artist) {
        return songRepository.findByArtist(artist); // Trả về danh sách bài hát của ca sĩ
    }
    @GetMapping("/getAllPlaylists")
    public List<playlists> getAllPlaylists() {
        return playlistRepository.findByCreatedBy(); // Trả về danh sách playlist
    }
    @GetMapping("/getPlaylistById")
    public playlists getPlaylistById(@RequestParam String playlistId) {
        return playlistRepository.findById(playlistId).get(); // Trả về playlist theo id
    }
    @GetMapping("/getPlaylistByUserId")
    public List<playlists> getPlaylistsByUserId(@RequestParam String userId) {
        return playlistRepository.findByUserId(userId); // Trả về playlist theo id
    }
    @GetMapping("/getPlaylistByUserIdAndSongId")
    public List<playlists> getPlaylistsByUserIdAndSongId(@RequestParam String userId, @RequestParam Long songId) {
        // Lấy tất cả playlists của userId
        List<playlists> playlists = playlistRepository.findByUserId(userId);

        // Lọc ra danh sách playlists không chứa bài hát với songId
        List<playlists> filteredPlaylists = new ArrayList<>();
        for (playlists playlist : playlists) {
            List<Long> songIds = playlistsongRepository.findSongIdByPlaylistId(playlist.getPlaylistId());
            if (!songIds.contains(songId)) { // Nếu playlist không chứa songId
                filteredPlaylists.add(playlist); // Thêm vào danh sách kết quả
            }
        }
        return filteredPlaylists; // Trả về danh sách đã lọc
    }
    @GetMapping("/createPlaylist")
    public String createPlaylist(@RequestParam String userId) {
            // Đếm số lượng danh sách phát hiện có của người dùng
            long count = playlistRepository.countByUserId(userId);
            // Tạo tên mặc định theo thứ tự
            String playlistName = "Danh sách phát " + (count + 1);
        String creator = userRepository.findByUserId(userId).getRole();
        // Tạo danh sách phát mới
        playlists playlist = playlists.builder()
                .playlistName(playlistName)
                .userId(userId)
                .playlistPicUrl("/img/PlaylistDefaultAvatar.png")
                .createdAt(LocalDate.now())
                .createdBy(creator)
                .totalTracks(0)
                .build();
        playlistRepository.save(playlist);
        return "1"; // Trả về thành công
    }
    @GetMapping("/deletePlaylist")
    public String deletePlaylist(@RequestParam String playlistId) {
        playlistRepository.deleteById(playlistId); // Xóa playlist
        return "1"; // Trả về thành công
    }
    @GetMapping("/addSongToPlaylist")
    public String addSongToPlaylist(@RequestParam String playlistId, @RequestParam Long songId) {
        playlistSongs playlistsong = playlistsongRepository.findByPlaylistIdAndSongId(playlistId, songId);
        if (playlistsong == null) {
            playlistsongRepository.save(playlistsong.builder().playlistId(playlistId).songId(songId).build());
            playlists playlist = playlistRepository.findById(playlistId).get();
            playlist.setTotalTracks(playlist.getTotalTracks() + 1);
            playlistRepository.save(playlist);
        }
        return "1"; // Trả về thành công
    }
    @GetMapping("/removeSongFromPlaylist")
    public String removeSongFromPlaylist(@RequestParam String playlistId, @RequestParam Long songId) {
        playlistsongRepository.deleteByPlaylistIdAndSongId(playlistId, songId);
        playlists playlist = playlistRepository.findById(playlistId).get();
        playlist.setTotalTracks(playlist.getTotalTracks() - 1);
        playlistRepository.save(playlist);
        return "1"; // Trả về thành công
    }
    @GetMapping("/getPlaylistSongs")
    public List<songs> getPlaylistSongs(@RequestParam String playlistId) {
        List<Long> songIds = playlistsongRepository.findSongIdByPlaylistId(playlistId);
        return songRepository.findAllById(songIds); // Trả về danh sách bài hát trong playlist
    }
    @GetMapping("/getAllGenres")
    public List<genres> getAllGenres() {
        return genreRepository.findAll(); // Trả về danh sách thể loại
    }
    @GetMapping("/getGenreSongs")
    public List<songs> getGenreSongs(@RequestParam String genre) {
        return songRepository.findByGenre(genre); // Trả về danh sách bài hát theo thể loại
    }
}
