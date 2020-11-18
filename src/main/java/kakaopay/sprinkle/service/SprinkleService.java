package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 뿌리기
 *
 * @author najisu
 * @version 2.0
 * @since 2020.11.17
 */
@Service
@RequiredArgsConstructor
public class SprinkleService {

    private final RoomJoinService roomJoinService;
    private final ReceiveService receiveService;

    private final SprinkleRepository sprinkleRepository;

    /**
     * 신규 뿌리기
     *
     * @param userId 유저 식별값
     * @param roomId 채팅방 식별값
     * @param sprinkleRequest 뿌리기
     * @return token 토큰
     */
    @Transactional
    public String newSprinkle(Long userId, Long roomId, SprinkleRequest sprinkleRequest) {

        roomJoinService.isJoinedUser(userId, roomId);

        String token = TokenUtil.generate();

        Sprinkle newSprinkle = Sprinkle.builder().roomId(roomId)
                .userId(userId)
                .numberOfRecipients(sprinkleRequest.getNumberOfRecipients())
                .token(token)
                .build();

        Sprinkle savedSprinkle = sprinkleRepository.save(newSprinkle);

        receiveService.initReceiveInfo(savedSprinkle.getId(), sprinkleRequest.getAmount(), sprinkleRequest.getNumberOfRecipients());

        return token;
    }

    public String receive(long userId, long roomId, String token) {

        // token으로 뿌리기 데이터 조회

        // 뿌리기한 사람이 요청한건지

        // 뿌리기하고 10분이 지났는지

        // 받기 요청한 유저가 같은방에 있는지

        // userId가 이미 받기한게 있는지 조회

        // 뿌리기 식별값으로 아직 받지않은 받기 데이터 1개 조회

        // 있으면 userId update하고

        // 없으면 Exception

        return "";
    }

    public Sprinkle check(long roomId, String token) {

        return sprinkleRepository.findByRoomId(roomId);
    }

}
