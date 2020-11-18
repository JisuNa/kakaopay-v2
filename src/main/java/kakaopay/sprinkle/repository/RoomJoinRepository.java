package kakaopay.sprinkle.repository;

import kakaopay.sprinkle.entity.RoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomJoinRepository extends JpaRepository<RoomJoin, Long> {
    Optional<RoomJoin> findByUserIdAndRoomId(Long userId, Long roomId);
}
