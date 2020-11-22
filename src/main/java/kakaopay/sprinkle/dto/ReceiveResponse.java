package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ReceiveResponse {

    private Long userId;
    private BigDecimal amount;

    public ReceiveResponse(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
