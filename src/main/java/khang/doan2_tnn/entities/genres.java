package khang.doan2_tnn.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class genres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long genreId;
    @Column(columnDefinition = "nvarchar(255)")
    String genreName;
}
