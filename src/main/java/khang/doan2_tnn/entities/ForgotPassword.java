package khang.doan2_tnn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpId;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationDate;

    @OneToOne
    private users user;


}
