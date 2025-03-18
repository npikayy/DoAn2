package khang.doan2_tnn.controllers;

import khang.doan2_tnn.services.genreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("admin/genre_management")
public class genreController {
    @Autowired
    private genreService genreService;

    @GetMapping()
    public ModelAndView getAllGenres(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/genre/genre_management");
        modelAndView.addObject("genres", genreService.getAllGenres());
        return modelAndView;
    }
    @GetMapping("/addGenre")
    public ModelAndView addGenre(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/genre/addGenre");
        return modelAndView;
    }
    @PostMapping("/addGenre")
    public ModelAndView addGenre(String genreName) {
        genreService.addGenre(genreName);
        return new ModelAndView("redirect:/admin/genre_management");
    }

    @GetMapping("/editGenre")
    public ModelAndView editGenre(ModelAndView modelAndView, @RequestParam Long genreId) {
        modelAndView.setViewName("admin/genre/editGenre");
        modelAndView.addObject("genre", genreService.getGenreById(genreId));
        return modelAndView;
    }
    @PostMapping("/editGenre")
    public ModelAndView editGenre(@RequestParam Long genreId, String genreName) {
        genreService.updateGenre(genreId, genreName);
        return new ModelAndView("redirect:/admin/genre_management");
    }

    @GetMapping("/deleteGenre")
    public ModelAndView deleteGenre(@RequestParam Long genreId) {
        genreService.deleteGenre(genreId);
        return new ModelAndView("redirect:/admin/genre_management");
    }
}
