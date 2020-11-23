package kakaopay.sprinkle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ReceiveResponse {
    private final Long userId;
    private final BigDecimal amount;
}
