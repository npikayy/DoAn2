package khang.doan2_tnn.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class songs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long songId;
    @Column(columnDefinition = "nvarchar(255)")
    String songName;
    @Column(columnDefinition = "nvarchar(255)")
    String artist;
    @Column(columnDefinition = "nvarchar(255)")
    String album;
    @Column(columnDefinition = "nvarchar(255)")
    String genre;
    @Column(columnDefinition = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    String duration;
    @Column(columnDefinition = "nvarchar(255)")
    String songPicUrl;
    @Column(columnDefinition = "nvarchar(255)")
    String SongUrl;


}
