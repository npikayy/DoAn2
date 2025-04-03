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
public class users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;
    @Column(columnDefinition = "nvarchar(255)")
    String fullName;
    String username;
    String password;
    String email;
    String role;
    @Column(columnDefinition = "nvarchar(255)")
    String userPicUrl;
    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;
}
