package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.userRepository;
import khang.doan2_tnn.services.playlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("admin/playlist_management")
public class playlistController {
    @Autowired
    private playlistService playlistService;
    @Autowired
    private userRepository userRepository;

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
    @GetMapping("addPlaylist")
    public ModelAndView addPlaylist(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/playlist/addPlaylist");
        return modelAndView;
    }
    @PostMapping("addPlaylist")
    public ModelAndView addPlaylist(String playlistName, MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        playlistService.addPlaylist(playlistName, user.getUserId(), imageFile);
        return new ModelAndView("redirect:/admin/playlist_management");
    }

}
