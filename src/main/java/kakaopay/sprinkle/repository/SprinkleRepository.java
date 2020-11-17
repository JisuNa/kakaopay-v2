package kakaopay.sprinkle.repository;

import kakaopay.sprinkle.entity.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SprinkleRepository extends JpaRepository<Sprinkle, Long> {

    Sprinkle findByRoomId(long roomId);

}
