package kakaopay.sprinkle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class InquiryResponse {
    private final LocalDateTime sprinkledAt;
    private final BigDecimal sprinkledAmount;
    private final BigDecimal receivedAmount;
    private final List<ReceiveResponse> receivedInfo;
}
