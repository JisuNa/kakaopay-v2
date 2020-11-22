package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SprinkleRequest {

    private int amount;
    private int numberOfRecipients;

    public SprinkleRequest(int amount, int numberOfRecipients) {
        this.amount = amount;
        this.numberOfRecipients = numberOfRecipients;
    }

}
