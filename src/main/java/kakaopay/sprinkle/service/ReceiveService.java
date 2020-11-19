package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.entity.Receive;
import kakaopay.sprinkle.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 받기
 *
 * @author najisu
 * @version 2.0
 * @since 2020.11.18
 */
@Service
@RequiredArgsConstructor
public class ReceiveService {

    private final ReceiveRepository receiveRepository;

    /**
     * 신규 뿌리기의 받기 초기값 할당
     *
     * @param sprinkleId         뿌리기 식별값
     * @param amount             뿌리기 전체금액
     * @param numberOfRecipients 뿌리기 받는 인원 수
     */
    public void initReceiveInfo(long sprinkleId, int amount, int numberOfRecipients) {

        int total_receive = 0;

        for (int i = 0; i < numberOfRecipients; i++) {

            int splitAmount = 0;

            if (i != numberOfRecipients - 1) {
                splitAmount = splitReceiveAmount(amount - total_receive);
                total_receive += splitAmount;
            } else {
                splitAmount = amount - total_receive;
            }

            receiveRepository.save(
                    Receive.builder().amount(splitAmount).sprinkleId(sprinkleId).build()
            );
        }

    }

    /**
     * @param receiveList 받기 리스트
     * @param userId      유저 식별값
     * @return amount 받기 금액
     */
    public int setReceiverProcess(List<Receive> receiveList, Long userId) {

        int receivedAmount = 0;
        int checkCompleteAmount = 0;

        // Todo 삭제 (stream으로 처리해보고 싶었지만 실패.)
        for (Receive receive : receiveList) {

            if (Objects.isNull(receive.getUserId())) {
                // 받기 안된거면 유저할당 업데이트
                receive.updateUserId(userId);
                receivedAmount = receiveRepository.save(receive).getAmount();
                break;
            } else if (receive.getUserId().equals(userId)) {
                // userId로 할당된게 있으면
                throw new ReceiveFailedException("해당 유저는 이미 받은 이력이 존재합니다.");
            }

            checkCompleteAmount += receive.getAmount();
        }

        return receivedAmount;
    }

//    /**
//     * 받기 받는사람 지정 프로세스
//     *
//     * @param sprinkleId 뿌리기 식별값
//     * @param userId     유저 식별값
//     * @return amount 받기 금액
//     */
//    public int setReceiverProcess(Long sprinkleId, Long userId) {
//
//        // userId가 이미 받기한게 있는지 조회
//        checkAlreadyReceive(sprinkleId, userId);
//
//        // 뿌리기 식별값으로 아직 받지않은 받기 데이터 1개 조회
//        Receive willSetReceiver = receiveRepository.findTop1ByIdAndUserIdIsNull(sprinkleId).orElseThrow(()
//                -> new ReceiveFailedException("해당 뿌리기는 모두 받아갔습니다.")
//        );
//
//        willSetReceiver.updateUserId(userId);
//        Receive receiver = receiveRepository.save(willSetReceiver);
//
//        return receiver.getAmount();
//    }

    /**
     * 받기 분할
     *
     * @param remainAmount 남은금액
     * @return receiveAmount 받기 분할된 금액
     */
    private int splitReceiveAmount(int remainAmount) {
        return (int) (Math.random() * remainAmount) + 1;
    }

    public void checkAlreadyReceive(Long sprinkleId, Long userId) {

        receiveRepository.findBySprinkleIdAndUserId(sprinkleId, userId).ifPresent(value -> {
            throw new ReceiveFailedException("해당 유저는 이미 받기를 했습니다.");
        });
    }

}
