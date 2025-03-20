package khang.doan2_tnn.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Builder
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
    @Column(columnDefinition = "nvarchar(255)")
    String playlistPicUrl;
}
