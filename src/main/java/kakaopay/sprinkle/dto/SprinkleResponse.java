package kakaopay.sprinkle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SprinkleResponse {
    private final Long userId;
    private final Long roomId;
    private final String token;
}
