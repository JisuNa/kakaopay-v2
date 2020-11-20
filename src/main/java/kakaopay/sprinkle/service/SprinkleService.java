package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.common.error.exception.ExpiredTokenException;
import kakaopay.sprinkle.common.error.exception.NotSprinklerInquiryException;
import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.ReceiveResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        // 해당 유저가 해당 채팅방에 있는지
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

    /**
     * 받기
     *
     * @param userId 유저 식별값
     * @param roomId 채팅방 식별값
     * @param token  토큰
     * @return {@link ReceiveResponse}
     */
    public ReceiveResponse receive(Long userId, Long roomId, String token) {

        // 받기 요청한 유저가 뿌리기한 방에 있는지
        roomJoinService.checkJoinedUser(userId, roomId);

        // token으로 뿌리기 데이터 조회
        Sprinkle sprinkle = getSprinkleByToken(token);

        // 받기 가능한지 검사
        checkCanReceive(userId, sprinkle);

        // 받기 유저 지정 프로세스
        int receiveAmount = receiveService.setReceiverProcess(sprinkle.getReceiveList(), userId);

        if (receiveAmount == 0) {
            throw new ReceiveFailedException("해당 뿌리기는 받을 수 있는 금액이 없습니다.");
        }

        return new ReceiveResponse(userId, receiveAmount);
    }

    /**
     * 뿌리기 조회
     *
     * @param userId 유저 식별값
     * @param token  토큰
     * @return {@link Sprinkle}
     */
    @Transactional(readOnly = true)
    public Sprinkle inquiry(Long userId, String token) {

        Sprinkle sprinkle = sprinkleRepository.findByToken(token).orElseThrow(()
                -> new EmptyInfoException("해당 토큰의 뿌리기가 존재하지 않습니다.")
        );

        // 뿌린 사람 자신만 조회를 할 수 있습니다.
        if (!userId.equals(sprinkle.getUserId())) {
            throw new NotSprinklerInquiryException("뿌리기한 유저만 조회가 가능합니다.");
        }

        // 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다
        checkExpired(sprinkle.getCreatedAt(), 10080);

        return sprinkle;
    }

    /**
     * 토큰으로 뿌리기 정보 조회
     *
     * @param token 토큰
     * @return {@link Sprinkle}
     */
    private Sprinkle getSprinkleByToken(String token) {

        return sprinkleRepository.findByToken(token).orElseThrow(()
                -> new EmptyInfoException("해당 토큰의 뿌리기 데이터가 존재하지 않습니다.")
        );
    }

    /**
     * 받기할 수 있는지 확인
     *
     * @param userId   유저 식별값
     * @param sprinkle 뿌리기 정보
     */
    private void checkCanReceive(Long userId, Sprinkle sprinkle) {

        // 뿌리기한 사람이 요청한건지
        if (sprinkle.getUserId().equals(userId)) {
            throw new ReceiveFailedException("뿌리기를 한 사람은 받기를 할 수 없습니다.");
        }

        // 뿌리기하고 10분이 지났는지
        checkExpired(sprinkle.getCreatedAt(), 10);
    }

    /**
     * 토큰 만료 확인
     *
     * @param createdAt 생성시간
     * @param min       분
     */
    private void checkExpired(LocalDateTime createdAt, int min) {

        if (createdAt.isBefore(LocalDateTime.now().minusMinutes(min))) {
            throw new ExpiredTokenException("사용할 수 없는 토큰 입니다.");
        }
    }

}
