package khang.doan2_tnn.repositories;

import khang.doan2_tnn.entities.comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentRepository extends JpaRepository<comments, Long> {
}
