package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.entity.RoomJoin;
import kakaopay.sprinkle.repository.RoomJoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomJoinService {

    private final RoomJoinRepository roomJoinRepository;

    public void isJoinedUser(Long userId, Long roomId) {
        Optional<RoomJoin> roomJoin = roomJoinRepository.findByUserIdAndRoomId(userId, roomId);

        roomJoin.orElseThrow(()
                -> new EmptyInfoException("해당유저는 채팅방에 참여하지 않았습니다.")
        );
    }

}
