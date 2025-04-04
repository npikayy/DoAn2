package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.services.songService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import khang.doan2_tnn.repositories.userRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/songs_management")
public class songController {
    @Autowired
    private songService songService;
    @Autowired
    private userRepository userRepository;

    @GetMapping("/searchSongs")
    public ModelAndView getSongByNameorArtistorGenre(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String artist,
                                                    @RequestParam(required = false) String genre, ModelAndView modelAndView)
    {
        List<songs> songs = songService.findByNameorArtistorGenre(name, artist, genre);
        modelAndView.setViewName("/admin/song/searchPage");
        modelAndView.addObject("songs",songs);
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView page(ModelAndView modelAndView){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        modelAndView.addObject("user",user.getRole());
        modelAndView.setViewName("/admin/song/mainPage");
        modelAndView.addObject("song",songService.findAll());
        return modelAndView;
    }
    @GetMapping("/addSong")
    public ModelAndView addAsong(ModelAndView modelAndView){
        modelAndView.setViewName("/admin/song/addSong");
        return modelAndView;
    }
    @PostMapping("/addSong")
    public ModelAndView addSong(String songName,
                                String artist,
                                String album,
                                String genre,
                                LocalDate releaseDate,
                                MultipartFile musicFile,
                                MultipartFile coverImage)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        songService.uploadMusic(musicFile, coverImage, songName, artist, album, genre, releaseDate, user.getUsername(),user.getUsername());
        return new ModelAndView("redirect:/admin/songs_management");
    }
    @GetMapping("/updateSong")
    public ModelAndView updateSong(ModelAndView modelAndView, @RequestParam String songId) {
        modelAndView.setViewName("/admin/song/updateSong");
        songs song = songService.findById(Long.parseLong(songId));
        modelAndView.addObject("song", song);
        return modelAndView;
    }
    @GetMapping("/updateSongFile")
    public ModelAndView updateSongFile(ModelAndView modelAndView, @RequestParam String songId) {
        modelAndView.setViewName("/admin/song/updateSongFile");
        modelAndView.addObject("songId", songId);
        return modelAndView;
    }
    @GetMapping("/updatePicFile")
    public ModelAndView updatePicFile(ModelAndView modelAndView, @RequestParam String songId) {
        modelAndView.setViewName("/admin/song/updatePicFile");
        modelAndView.addObject("songId", songId);
        return modelAndView;
    }
    @PostMapping("/updateSong")
    public ModelAndView updateSong(@RequestParam String songId, String songName, String artist, String album, String genre, LocalDate releaseDate) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        songService.updateMusic(Long.parseLong(songId), songName, artist, album, genre, releaseDate, user.getUsername());
        return new ModelAndView("redirect:/admin/songs_management");
    }
    @PostMapping("/updateSongFile")
    public ModelAndView updateSongFile(@RequestParam String songId, MultipartFile musicFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        songService.updateMusicFile(musicFile, Long.parseLong(songId), user.getUsername());
        return new ModelAndView("redirect:/admin/songs_management");
    }

    @PostMapping("/updatePicFile")
    public ModelAndView updatePicFile(@RequestParam String songId, MultipartFile coverImage) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        songService.updatePicFile(coverImage, Long.parseLong(songId), user.getUsername());
        return new ModelAndView("redirect:/admin/songs_management");
    }
    @GetMapping("/deleteSong")
    public ModelAndView deleteSong(@RequestParam String songId)
    {
        songService.deleteById(Long.parseLong(songId));
        return new ModelAndView("redirect:/admin/songs_management");
    }
}
