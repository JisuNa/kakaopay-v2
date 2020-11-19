package kakaopay.sprinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SprinkleRequest {

    private int amount;
    private int numberOfRecipients;

}
