package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.playlists;
import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.songRepository;
import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.services.playlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/playlist_management")
public class playlistController {
    @Autowired
    private playlistService playlistService;
    @Autowired
    private userRepository userRepository;
    @Autowired
    private songRepository songRepository;
    @Autowired
    private khang.doan2_tnn.repositories.playlistsongRepository playlistsongRepository;
    @GetMapping()
    public ModelAndView getPlaylists(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        modelAndView.addObject("user", user.getRole());
        modelAndView.addObject("playlists", playlistService.getPlaylists());
        modelAndView.setViewName("admin/playlist/playlist_management");
        return modelAndView;
    }
//    public List<Long> extractSongIds(List<songs> songList) {
//        return songList.stream() // Sử dụng Stream API
//                .map(songs::getSongId) // Lấy id từ mỗi đối tượng songs
//                .collect(Collectors.toList()); // Thu thập vào danh sách
//    }
@GetMapping("/searchSongsToAdd")
public ModelAndView getSongsNotInPlaylist(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String artist,
        @RequestParam(required = false) String genre,
        @RequestParam String playlistId) {
    // Fetch song IDs already in the playlist
    List<Long> danhSachBaiHat = playlistsongRepository.findSongIdByPlaylistId(playlistId);

    // Find matching songs not in the playlist
    List<songs> songsNotInPlaylist = songRepository.findByNameOrArtistOrGenreAndNotInPlaylist(
            name, artist, genre, danhSachBaiHat
    );

    // Return ModelAndView with data
    return new ModelAndView("/admin/playlist/searchToAdd")
            .addObject("songs", songsNotInPlaylist)
            .addObject("playlistId", playlistId);
}
    @GetMapping("/addPlaylist")
    public ModelAndView addPlaylist(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/playlist/addPlaylist");
        return modelAndView;
    }
    @PostMapping("/addPlaylist")
    public ModelAndView addPlaylist(String playlistName, MultipartFile coverImage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        playlistService.addPlaylist(playlistName, user.getUserId(), coverImage);
        return new ModelAndView("redirect:/admin/playlist_management");
    }
    @GetMapping("/playlistSongs")
    public ModelAndView playlistSongs(ModelAndView modelAndView, @RequestParam String playlistId){
        playlists playlist = playlistService.getPlaylistById(playlistId);
        List<Long> danhSachBaiHat = playlistsongRepository.findSongIdByPlaylistId(playlistId);
        List<songs> songs = songRepository.findAllById(danhSachBaiHat);
        users user = userRepository.findByUserId(playlist.getUserId());
        modelAndView.addObject("user", user.getFullName());
        modelAndView.addObject("songs", songs);
        modelAndView.setViewName("admin/playlist/playlistSongs");
        modelAndView.addObject("playlist",playlist);
        return modelAndView;
    }
    @GetMapping("/songList")
    public ModelAndView songList(ModelAndView modelAndView, @RequestParam String playlistId){
        List<Long> danhSachBaiHat = playlistsongRepository.findSongIdByPlaylistId(playlistId);
        List<songs> songs = songRepository.findSongIdNotIn(danhSachBaiHat);
        modelAndView.addObject("songs", songs);
        modelAndView.setViewName("admin/playlist/addSongToPlaylist");
        modelAndView.addObject("playlistId",playlistId);
        return modelAndView;
    }
    @GetMapping("/addSongToPlaylist")
    public ModelAndView addSongToPlaylist(@RequestParam String songId, @RequestParam String playlistId) {
        playlistService.addSongToPlaylist(playlistId,songId);
        return new ModelAndView("redirect:/admin/playlist_management/playlistSongs?playlistId="+playlistId);
    }
    @GetMapping("/editPlaylistName")
    public ModelAndView editPlaylist(@RequestParam String playlistId, ModelAndView modelAndView) {
        playlists playlist = playlistService.getPlaylistById(playlistId);
        modelAndView.addObject("playlist", playlist);
        modelAndView.setViewName("admin/playlist/editPlaylistName");
        return modelAndView;
    }
    @PostMapping("/editPlaylistName")
    public ModelAndView editPlaylist(@RequestParam String playlistId, String playlistName) {
        playlistService.updatePlaylistName(playlistId, playlistName);
        return new ModelAndView("redirect:/admin/playlist_management");
    }
    @GetMapping("/editPlaylistCoverImage")
    public ModelAndView editPlaylistCoverImage(@RequestParam String playlistId, ModelAndView modelAndView) {
        playlists playlist = playlistService.getPlaylistById(playlistId);
        modelAndView.addObject("playlist", playlist);
        modelAndView.setViewName("admin/playlist/editPlaylistCoverImage");
        return modelAndView;
    }
    @PostMapping("/editPlaylistCoverImage")
    public ModelAndView editPlaylistCoverImage(@RequestParam String playlistId, MultipartFile coverImage) {
        playlistService.updatePlaylistCoverImage(playlistId, coverImage);
        return new ModelAndView("redirect:/admin/playlist_management");
    }
    @GetMapping("/deletePlaylist")
    public ModelAndView deletePlaylist(@RequestParam String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return new ModelAndView("redirect:/admin/playlist_management");
    }
    @GetMapping("/deleteSongFromPlaylist")
    public ModelAndView deleteSongFromPlaylist(@RequestParam String playlistId, @RequestParam String songId) {
        playlistService.deleteSongFromPlaylist(playlistId, Long.parseLong(songId));
        return new ModelAndView("redirect:/admin/playlist_management/playlistSongs?playlistId="+playlistId);
    }
}
