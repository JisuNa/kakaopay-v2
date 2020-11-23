package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.common.error.exception.ExpiredTokenException;
import kakaopay.sprinkle.common.error.exception.NotSprinklerInquiryException;
import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.common.util.TokenUtil;
import kakaopay.sprinkle.dto.InquiryResponse;
import kakaopay.sprinkle.dto.ReceiveResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.dto.SprinkleResponse;
import kakaopay.sprinkle.entity.Receive;
import kakaopay.sprinkle.entity.Sprinkle;
import kakaopay.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static final int SEVEN_DAYS_MIN = 10080;
    private final RoomJoinService roomJoinService;
    private final ReceiveService receiveService;
    private final SprinkleRepository sprinkleRepository;

    /**
     * 신규 뿌리기
     *
     * @param userId          유저 식별값
     * @param roomId          채팅방 식별값
     * @param sprinkleRequest 뿌리기
     * @return {@link SprinkleResponse}
     */
    @Transactional
    public SprinkleResponse newSprinkle(Long userId, Long roomId, SprinkleRequest sprinkleRequest) {

        // 해당 유저가 해당 채팅방에 있는지
        roomJoinService.checkJoinedUser(userId, roomId);

        final String token = TokenUtil.generate();

        Sprinkle newSprinkle = Sprinkle.builder().roomId(roomId)
                .userId(userId)
                .amount(sprinkleRequest.getAmount())
                .numberOfRecipients(sprinkleRequest.getNumberOfRecipients())
                .token(token)
                .build();

        Sprinkle savedSprinkle = sprinkleRepository.save(newSprinkle);

        receiveService.initReceiveInfo(savedSprinkle.getId(), sprinkleRequest.getAmount(), sprinkleRequest.getNumberOfRecipients());

        return SprinkleResponse.builder().userId(userId).roomId(roomId).token(token).build();
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

        roomJoinService.checkJoinedUser(userId, roomId);

        Sprinkle sprinkle = getSprinkleByToken(token, roomId);

        checkCanReceive(userId, sprinkle);

        BigDecimal receiveAmount = receiveService.setReceiverProcess(sprinkle.getReceiveList(), userId);

        updateSprayedAmount(sprinkle, receiveAmount);

        return new ReceiveResponse(userId, receiveAmount);
    }

    /**
     * 뿌리기 조회
     *
     * @param userId 유저 식별값
     * @param roomId 채팅방 식별값
     * @param token  토큰
     * @return {@link Sprinkle}
     */
    @Transactional(readOnly = true)
    public InquiryResponse inquiry(Long userId, Long roomId, String token) {

        Sprinkle sprinkle = getSprinkleByToken(token, roomId);

        checkSprinkleUserInquiry(userId, sprinkle.getUserId());

        checkExpired(sprinkle.getCreatedAt(), SEVEN_DAYS_MIN);

        List<ReceiveResponse> receivedInfo = manufacturingInquiryReceivedInfo(sprinkle.getReceiveList());

        return InquiryResponse.builder().sprinkledAt(sprinkle.getCreatedAt())
                .sprinkledAmount(sprinkle.getAmount())
                .receivedAmount(sprinkle.getSprayedAmount()).receivedInfo(receivedInfo).build();
    }

    /**
     * 토큰으로 뿌리기 정보 조회
     *
     * @param token 토큰
     * @return {@link Sprinkle}
     */
    private Sprinkle getSprinkleByToken(String token, Long roomId) {

        return sprinkleRepository.findByTokenAndRoomId(token, roomId).orElseThrow(()
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

        checkSprinkleUserReceive(userId, sprinkle.getUserId());

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

    /**
     * 뿌려진 금액 업데이트
     *
     * @param sprinkle      뿌리기 정보
     * @param sprayedAmount 받기 금액
     */
    private void updateSprayedAmount(Sprinkle sprinkle, BigDecimal sprayedAmount) {
        sprinkle.updateSprayedAmount(sprayedAmount.add(sprinkle.getSprayedAmount()));
        sprinkleRepository.save(sprinkle);
    }

    /**
     * 뿌린 사람 자신이 조회한건지 체크
     *
     * @param requestUserId 조회 요청한 유저 식별값
     * @param sprinkleUserId 뿌리기 유저 식별값
     */
    private void checkSprinkleUserInquiry(Long requestUserId, Long sprinkleUserId) {
        if (!requestUserId.equals(sprinkleUserId)) {
            throw new NotSprinklerInquiryException("뿌리기한 유저만 조회가 가능합니다.");
        }
    }

    /**
     * 뿌린 사람 자신이 받기하는지 체크
     *
     * @param requestUserId 받기 요청한 유저 식별값
     * @param sprinkleUserId 뿌리기 유저 식별값
     */
    private void checkSprinkleUserReceive(Long requestUserId, Long sprinkleUserId) {
        if (requestUserId.equals(sprinkleUserId)) {
            throw new ReceiveFailedException("뿌리기를 한 사람은 받기를 할 수 없습니다.");
        }
    }

    /**
     * 조회 받기 완료된 정보 가공
     *
     * @param receiveList 받기 정보
     * @return {@link ReceiveResponse}
     */
    private List<ReceiveResponse> manufacturingInquiryReceivedInfo(List<Receive> receiveList) {
        List<ReceiveResponse> receivedInfoList = new ArrayList<>();

        for (Receive receive : receiveList) {
            if (receive.getUserId() != null) {
                ReceiveResponse receiveInfo = ReceiveResponse.builder().userId(receive.getUserId()).amount(receive.getAmount()).build();
                receivedInfoList.add(receiveInfo);
            }
        }
        return receivedInfoList;
    }

}
