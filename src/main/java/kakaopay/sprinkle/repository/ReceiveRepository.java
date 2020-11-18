package kakaopay.sprinkle.repository;

import kakaopay.sprinkle.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiveRepository extends JpaRepository<Receive, Long> {
}
