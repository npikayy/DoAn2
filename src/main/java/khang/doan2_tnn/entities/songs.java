package khang.doan2_tnn.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class songs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long songId;
    String songName;
    String artist;
    String album;
    String genre;
    LocalDate release_date;
    String duration;
    String path;
}
