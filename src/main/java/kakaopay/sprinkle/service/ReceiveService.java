package kakaopay.sprinkle.service;

import kakaopay.sprinkle.entity.Receive;
import kakaopay.sprinkle.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
     * 뿌리기 받기 초기값 설정
     *
     * @param sprinkleId 뿌리기 식별값
     * @param amount 뿌리기 전체금액
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
     * 받기 분
     *
     * @param remainAmount 남은금액
     * @return 받기 분할된 금액
     */
    private int splitReceiveAmount(int remainAmount) {
        return (int)(Math.random() * remainAmount)+1;
    }

}
