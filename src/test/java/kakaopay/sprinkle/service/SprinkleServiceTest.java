package kakaopay.sprinkle.service;

import kakaopay.sprinkle.dto.InquiryResponse;
import kakaopay.sprinkle.dto.ReceiveResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.dto.SprinkleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SprinkleServiceTest {
    private static final Long ROOM_ID = 1L;
    private static final Long SPRINKLE_USER_ID = 1L;
    private static final Long RECEIVE_USER_ID = 2L;
    private static final String TOKEN = "abc";
    private static final BigDecimal SPRINKLE_AMOUNT = BigDecimal.valueOf(5000);
    private static final BigDecimal RECEIVE_AMOUNT = BigDecimal.valueOf(2000);
    private static final int NUMBER_OF_RECIPIENTS = 3;
    @Autowired
    SprinkleService sprinkleService;

    @Test
    @DisplayName("뿌리기 성공")
    void successNewSprinkleTest() {
        // given
        SprinkleRequest sprinkleRequest = new SprinkleRequest(SPRINKLE_AMOUNT, NUMBER_OF_RECIPIENTS);

        // when
        final SprinkleResponse sprinkleResponse = sprinkleService.newSprinkle(SPRINKLE_USER_ID, ROOM_ID, sprinkleRequest);

        // then
        assertEquals(SPRINKLE_USER_ID, sprinkleResponse.getUserId());
        assertEquals(ROOM_ID, sprinkleResponse.getRoomId());
        assertNotNull(sprinkleResponse.getToken());
    }

    @Test
    @DisplayName("받기 성공")
    void successReceiveTest() {
        // given

        // when
        final ReceiveResponse receiveResponse = sprinkleService.receive(RECEIVE_USER_ID, ROOM_ID, TOKEN);

        // then
        assertEquals(RECEIVE_USER_ID, receiveResponse.getUserId());
        assertNotNull(receiveResponse.getAmount());
    }

    @Test
    @DisplayName("조회 성공")
    void successInquiryTest() {
        // given
        SprinkleRequest sprinkleRequest = new SprinkleRequest(SPRINKLE_AMOUNT, NUMBER_OF_RECIPIENTS);
        final SprinkleResponse sprinkleResponse = sprinkleService.newSprinkle(SPRINKLE_USER_ID, ROOM_ID, sprinkleRequest);

        // when
        final InquiryResponse inquiryResponse = sprinkleService.inquiry(SPRINKLE_USER_ID, ROOM_ID, sprinkleResponse.getToken());

        // then
        assertNotNull(inquiryResponse.getSprinkledAt());
        assertNotNull(inquiryResponse.getSprinkledAmount());
        assertNotNull(inquiryResponse.getReceivedAmount());
        assertNotNull(inquiryResponse.getReceivedInfo());
    }
}