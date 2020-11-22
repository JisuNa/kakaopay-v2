package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SprinkleRequest {

    private BigDecimal amount;
    private int numberOfRecipients;

    public SprinkleRequest(BigDecimal amount, int numberOfRecipients) {
        this.amount = amount;
        this.numberOfRecipients = numberOfRecipients;
    }

}
