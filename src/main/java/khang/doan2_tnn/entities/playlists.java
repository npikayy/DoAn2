package khang.doan2_tnn.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class playlists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long playlist_id;
    String playlist_name;
    String playlistDescription;
    Long userId;
    LocalDate created_at;
}
