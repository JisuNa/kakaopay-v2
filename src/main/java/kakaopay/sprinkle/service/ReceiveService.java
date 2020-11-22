package kakaopay.sprinkle.service;

import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import kakaopay.sprinkle.entity.Receive;
import kakaopay.sprinkle.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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
    public void initReceiveInfo(long sprinkleId, BigDecimal amount, int numberOfRecipients) {

        BigDecimal splitAmount = null;
        BigDecimal totalReceive = new BigDecimal(0);

        for (int i = 0; i < numberOfRecipients; i++) {

            if (i != numberOfRecipients - 1) {
                splitAmount = splitReceiveAmount(amount.subtract(totalReceive));
                totalReceive  = totalReceive.add(splitAmount);
            } else {
                splitAmount = amount.subtract(totalReceive);
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
    @Transactional
    public BigDecimal setReceiverProcess(List<Receive> receiveList, Long userId) {

        BigDecimal receivedAmount = new BigDecimal(0);

        for (Receive receive : receiveList) {

            if (receive.getUserId() == null) {
                // 받기 안된거면 유저할당 업데이트
                receive.updateUserId(userId);
                receivedAmount = receiveRepository.save(receive).getAmount();
                break;

            } else if (receive.getUserId().equals(userId)) {
                // userId로 할당된게 있으면
                throw new ReceiveFailedException("해당 유저는 이미 받은 이력이 존재합니다.");

            }
        }

        if (receivedAmount.equals(0)) {
            throw new ReceiveFailedException("해당 뿌리기는 받을 수 있는 금액이 없습니다.");
        }

        return receivedAmount;
    }

    /**
     * 받기 분할
     *
     * @param remainAmount 남은금액
     * @return receiveAmount 받기 분할된 금액
     */
    private BigDecimal splitReceiveAmount(BigDecimal remainAmount) {

        BigDecimal a = remainAmount.multiply(BigDecimal.valueOf(Math.random())).setScale(0, BigDecimal.ROUND_UP);

        return a;
    }

}
