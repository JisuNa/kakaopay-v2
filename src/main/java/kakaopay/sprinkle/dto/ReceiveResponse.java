package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReceiveResponse {

    private Long userId;
    private int amount;

    public ReceiveResponse(Long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
