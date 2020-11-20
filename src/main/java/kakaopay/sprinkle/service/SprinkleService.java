package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.ReceiveResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
     * @param userId          유저 식별값
     * @param roomId          채팅방 식별값
     * @param sprinkleRequest 뿌리기
     * @return token 토큰
     */
    @Transactional
    public String newSprinkle(Long userId, Long roomId, SprinkleRequest sprinkleRequest) {

        roomJoinService.checkJoinedUser(userId, roomId);

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

    public Sprinkle getSprinkleByToken(String token) {

        return sprinkleRepository.findByToken(token).orElseThrow(()
                -> new EmptyInfoException("해당 토큰의 뿌리기 데이터가 존재하지 않습니다.")
        );
    }

    public void checkCanReceive(long userId, long roomId, Sprinkle sprinkle) {

        // 뿌리기한 사람이 요청한건지
        if (sprinkle.getUserId() == userId) {
            throw new ReceiveFailedException("뿌리기를 한 사람은 받기를 할 수 없습니다.");
        }

        // 뿌리기하고 10분이 지났는지
        checkExpired(sprinkle.getCreatedAt(), 10);
    }

    public Sprinkle check(String token) {

        return sprinkleRepository.findByToken(token).orElseThrow(()
                -> new EmptyInfoException("해당 토큰의 뿌리기가 존재하지 않습니다.")
        );
    }

    private void checkExpired(LocalDateTime createdAt, int min) {

        if (createdAt.isBefore(LocalDateTime.now().minusMinutes(min))) {
            throw new ReceiveFailedException("만료된 뿌리기 입니다.");
        }
    }

    // Todo 삭제
    public Object test(String token) {

//        Receive notReceived = null;
//
//        for (Receive receive : getSprinkleByToken(token).getReceiveList()) {
//            if (Objects.isNull(receive.getUserId())) {
//                notReceived = receive;
//                break;
//            }
//        }
//
//        return notReceived;
        return sprinkleRepository.findByToken(token);
    }

}
