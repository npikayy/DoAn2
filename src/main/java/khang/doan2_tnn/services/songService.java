package khang.doan2_tnn.services;

import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.repositories.songRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class songService {

    @Autowired
    private songRepository songRepository;

    public songs save(songs song) {
        return songRepository.save(song);
    }

    public List<songs> findAll() {
        return songRepository.findAll();
    }

    public songs findById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

}
