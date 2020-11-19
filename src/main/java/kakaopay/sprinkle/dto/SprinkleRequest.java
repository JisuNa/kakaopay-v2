package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SprinkleRequest {

    private int amount;
    private int numberOfRecipients;

    // Todo 필요없으면 삭제
//    @Builder
//    public SprinkleRequest(BigInteger userId, String roomId, String token, int amount, int numberOfRecipients) {
//        this.userId = userId;
//        this.roomId = roomId;
//        this.token = token;
//        this.amount = amount;
//        this.numberOfRecipients = numberOfRecipients;
//    }

}
