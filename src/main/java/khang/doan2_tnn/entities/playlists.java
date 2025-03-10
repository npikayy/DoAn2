package khang.doan2_tnn.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    String playlistId;
    @Column(columnDefinition = "nvarchar(255)")
    String playlistName;
    String userId;
    @Column(columnDefinition = "Date")
    LocalDate createdAt;
}
