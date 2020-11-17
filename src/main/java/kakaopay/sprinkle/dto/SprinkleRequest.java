package kakaopay.sprinkle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class SprinkleRequest {

    private BigInteger userId;
    private String roomId;
    private String token;
    private int amount;
    private int numberOfRecipients;

    @Builder
    public SprinkleRequest(BigInteger userId, String roomId, String token, int amount, int numberOfRecipients, String status) {
        this.userId = userId;
        this.roomId = roomId;
        this.token = token;
        this.amount = amount;
        this.numberOfRecipients = numberOfRecipients;
    }

}
