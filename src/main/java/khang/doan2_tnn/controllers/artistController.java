package khang.doan2_tnn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("")
    public ModelAndView getArtists(ModelAndView modelAndView) {
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
    public ModelAndView addArtist(String artistName, String artistBio, MultipartFile imageFile) throws FileNotFoundException {
        artistService.addArtist(artistName, artistBio, imageFile);
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
    @PostMapping("/editArtist")
    public ModelAndView editArtist(@RequestParam String artistId,String artistName, String artistBio, MultipartFile imageFile) throws IOException {
        artistService.updateArtist(Long.parseLong(artistId), artistName, artistBio, imageFile);
        return new ModelAndView("redirect:/admin/artist_management");

    }
}
