package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.genres;
import khang.doan2_tnn.repositories.genreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class genreService {
    @Autowired
    private genreRepository genreRepository;

    public List<genres> getAllGenres() {
        return genreRepository.findAll();
    }
    public void addGenre(String genreName) {
        genres genre = new genres();
        genre.setGenreName(genreName);
        genreRepository.save(genre);
    }
    public void deleteGenre(Long genreId) {
        genreRepository.deleteById(genreId);
    }
    public void updateGenre(Long genreId, String genreName) {
        genres genre = genreRepository.findById(genreId).get();
        genre.setGenreName(genreName);
        genreRepository.save(genre);
    }
    public genres getGenreById(Long genreId) {
        return genreRepository.findById(genreId).get();
    }
}
