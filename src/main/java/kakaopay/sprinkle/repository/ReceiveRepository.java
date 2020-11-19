package kakaopay.sprinkle.repository;

import kakaopay.sprinkle.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiveRepository extends JpaRepository<Receive, Long> {
    Optional<Receive> findByIdAndUserId(Long id, Long userId);

    Optional<Receive> findBySprinkleIdAndUserId(Long sprinkleId, Long userId);
}
