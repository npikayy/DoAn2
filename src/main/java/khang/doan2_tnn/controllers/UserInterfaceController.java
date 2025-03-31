package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.*;
import khang.doan2_tnn.repositories.playlistRepository;
import khang.doan2_tnn.repositories.artistRepository;
import khang.doan2_tnn.repositories.playlistsongRepository;
import khang.doan2_tnn.repositories.songRepository;
import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.repositories.genreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
        return playlistRepository.findAll(); // Trả về danh sách playlist
    }
    @GetMapping("/getPlaylistById")
    public playlists getPlaylistById(@RequestParam String playlistId) {
        return playlistRepository.findById(playlistId).get(); // Trả về playlist theo id
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
