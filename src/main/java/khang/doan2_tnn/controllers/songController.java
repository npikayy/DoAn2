package khang.doan2_tnn.controllers;

import khang.doan2_tnn.entities.songs;
import khang.doan2_tnn.response.apiResponse;
import khang.doan2_tnn.services.songService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/songs")
public class songController {
    @Autowired
    private songService songService;

    @GetMapping
    public apiResponse<List<songs>> getAllSongs() {
        return apiResponse.<List<songs>>builder()
               .message("success")
               .result(songService.findAll())
                .build();
    }
//@GetMapping("/all")
//public ResponseEntity<?> getStudents() {
//    Map<String, Object> respSong = new LinkedHashMap<String, Object>();
//    List<songs> songList = songService.findAll();
//    log.warn("Song List: " + songList);
//    if (!songList.isEmpty()) {
//        respSong.put("status", 1);
//        respSong.put("data", songList);
//        return new ResponseEntity<>(respSong, HttpStatus.OK);
//    } else {
//        respSong.clear();
//        respSong.put("status", 0);
//        respSong.put("message", "Data is not found");
//        return new ResponseEntity<>(respSong, HttpStatus.NOT_FOUND);
//    }
//}


}
