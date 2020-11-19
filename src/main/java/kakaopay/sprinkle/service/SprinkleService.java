package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

    public String receive(Long userId, Long roomId, String token) {

        // token으로 뿌리기 데이터 조회
        Sprinkle sprinkle = getSprinkleByToken(token);

        // 받기 가능한지 검사
        checkCanReceive(userId, roomId, sprinkle);

        // 받기 요청한 유저가 같은방에 있는지
        roomJoinService.isJoinedUser(userId, roomId);

        // userId가 이미 받기한게 있는지 조회
        receiveService.checkAlreadyReceive(sprinkle.getId(), userId);

        // 뿌리기 식별값으로 아직 받지않은 받기 데이터 1개 조회


        // 있으면 userId update하고

        // 없으면 Exception

        return "";
    }

    public Sprinkle check(String token) {
        return sprinkleRepository.findByToken(token).orElseThrow(
                () -> new EmptyInfoException("해당 토큰의 뿌리기가 존재하지 않습니다.")
        );
    }

    private Sprinkle getSprinkleByToken(String token) {
        Optional<Sprinkle> getSprinkle = sprinkleRepository.findByToken(token);

        return getSprinkle.orElseThrow(
                () -> new EmptyInfoException("해당 토큰의 뿌리기가 존재하지 않습니다.")
        );
    }

    private void checkCanReceive(long userId, long roomId, Sprinkle sprinkle) {

        // 완료된 상태인지
        if ("PROCESSING".equals(sprinkle.getStatus())) {
            throw new ReceiveFailedException("해당 뿌리기는 이미 완료된 상태입니다.");
        }

        // 뿌리기한 사람이 요청한건지
        if (sprinkle.getUserId() == userId) {
            throw new ReceiveFailedException("뿌리기 신청한 사람은 받기를 할 수 없습니다.");
        }

        // 뿌리기하고 10분이 지났는지
        checkExpired(sprinkle.getCreatedAt(), 10);


    }

    private void checkExpired(LocalDateTime createdAt, int min) {
        if (60 * min > ChronoUnit.SECONDS.between(createdAt, LocalDateTime.now())) {
            throw new ReceiveFailedException("만료된 뿌리기 입니다.");
        }
    }

}
