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
public class comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long commentId;
    @Column(columnDefinition = "nvarchar(255)")
    String commentContent;
    String userId;
    Long songId;
    @Column(columnDefinition = "Date")
    LocalDate commentDate;
}
