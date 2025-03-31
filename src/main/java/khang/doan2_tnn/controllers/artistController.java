package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.users;
import khang.doan2_tnn.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import khang.doan2_tnn.services.artistService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/admin/artist_management")
public class artistController {
    @Autowired
    private artistService artistService;
    @Autowired
    private userRepository userRepository;

    @GetMapping("")
    public ModelAndView getArtists(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }
        users user = userRepository.findByUsername(username);
        modelAndView.addObject("user",user.getRole());
        modelAndView.addObject("artists", artistService.getArtists());
        modelAndView.setViewName("admin/artist/artist_management");
        return modelAndView;
    }
    @GetMapping("/addArtist")
    public ModelAndView addArtist(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/artist/addArtist");
        return modelAndView;
    }
    @PostMapping("/addArtist")
    public ModelAndView addArtist(String artistName, String artistBio, MultipartFile coverImage) throws FileNotFoundException {
        artistService.addArtist(artistName, artistBio, coverImage);
        return new ModelAndView("redirect:/admin/artist_management");
    }
    @GetMapping("/deleteArtist")
    public ModelAndView deleteSong(@RequestParam String artistId)
    {
        artistService.deleteArtist(Long.parseLong(artistId));
        return new ModelAndView("redirect:/admin/artist_management");
    }
    @GetMapping("/editArtist")
    public ModelAndView editArtist(ModelAndView modelAndView, @RequestParam String artistId)
    {
        modelAndView.setViewName("admin/artist/editArtist");
        modelAndView.addObject("artist", artistService.getArtistById(Long.parseLong(artistId)));
        return modelAndView;
    }
    @GetMapping("/editPic")
    public ModelAndView editPic(ModelAndView modelAndView, @RequestParam String artistId)
    {
        modelAndView.setViewName("admin/artist/editPic");
        modelAndView.addObject("artist", artistService.getArtistById(Long.parseLong(artistId)));
        return modelAndView;
    }
    @PostMapping("/editPic")
    public ModelAndView editPic(@RequestParam String artistId, MultipartFile coverImage) throws IOException {
        artistService.updatePicUrl(Long.parseLong(artistId), coverImage);
        return new ModelAndView("redirect:/admin/artist_management");
    }
    @PostMapping("/editArtist")
    public ModelAndView editArtist(@RequestParam String artistId,String artistName, String artistBio) throws IOException {
        artistService.updateArtist(Long.parseLong(artistId), artistName, artistBio);
        return new ModelAndView("redirect:/admin/artist_management");
    }
}
