package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.entity.Receive;
import kakaopay.sprinkle.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 받기 서비스
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
     * 받기 유저 지정 프로세스
     *
     * @param receiveList 받기 리스트
     * @param userId      받기 유저
     * @return receivedAmount 받은 금액
     */
    public int setReceiverProcess(List<Receive> receiveList, Long userId) {

        int receivedAmount = 0;

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
        }

        return receivedAmount;
    }

    /**
     * 받기 분할
     *
     * @param remainAmount 남은금액
     * @return receiveAmount 받기 분할된 금액
     */
    private int splitReceiveAmount(int remainAmount) {
        return (int) (Math.random() * remainAmount) + 1;
    }

}
