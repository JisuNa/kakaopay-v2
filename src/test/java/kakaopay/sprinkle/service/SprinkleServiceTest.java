package kakaopay.sprinkle.service;

import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.dto.SprinkleResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SprinkleServiceTest {
    @Autowired
    SprinkleService sprinkleService;

    private static final Long ROOM_ID = 1L;
    private static final Long SPRINKLE_USER_ID = 1L;
    private static final Long RECEIVE_USER_ID = 2L;
    private static final String TOKEN = "abc";
    private static final BigDecimal SPRINKLE_AMOUNT = BigDecimal.valueOf(5000);
    private static final BigDecimal RECEIVE_AMOUNT = BigDecimal.valueOf(2000);
    private static final int NUMBER_OF_RECIPIENTS = 3;

    @Test
    @DisplayName("뿌리기 성공")
    void successNewSprinkle() throws Exception {
        // given
        SprinkleRequest sprinkleRequest = new SprinkleRequest(SPRINKLE_AMOUNT, NUMBER_OF_RECIPIENTS);

        // when
        final SprinkleResponse sprinkleResponse = sprinkleService.newSprinkle(SPRINKLE_USER_ID, ROOM_ID, sprinkleRequest);

        // then
        assertEquals(SPRINKLE_USER_ID, sprinkleResponse.getUserId());
        assertEquals(ROOM_ID, sprinkleResponse.getRoomId());
        assertNotNull(sprinkleResponse.getToken());
    }

}