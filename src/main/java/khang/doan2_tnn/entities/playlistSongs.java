package khang.doan2_tnn.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class playlistSongs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String playlistId;
    Long songId;
}
