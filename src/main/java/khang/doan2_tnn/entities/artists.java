package khang.doan2_tnn.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class artists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long artistId;
    @Column(columnDefinition = "nvarchar(255)")
    String artistName;
    @Column(columnDefinition = "nvarchar(255)")
    String artistBio;
    @Column(columnDefinition = "nvarchar(255)")
    String artistPicUrl;
}
