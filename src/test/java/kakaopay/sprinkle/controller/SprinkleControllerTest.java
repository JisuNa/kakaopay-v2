package kakaopay.sprinkle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaopay.sprinkle.common.constant.Code;
import kakaopay.sprinkle.dto.InquiryResponse;
import kakaopay.sprinkle.dto.ReceiveResponse;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.dto.SprinkleResponse;
import kakaopay.sprinkle.service.SprinkleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SprinkleController.class)
@AutoConfigureMockMvc
class SprinkleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SprinkleService sprinkleService;

    ObjectMapper mapper = new ObjectMapper();

    SprinkleRequest sprinkleRequest;

    private static final Long ROOM_ID = 1L;
    private static final Long SPRINKLE_USER_ID = 1L;
    private static final Long RECEIVE_USER_ID = 2L;
    private static final String TOKEN = "abc";
    private static final BigDecimal SPRINKLE_AMOUNT = BigDecimal.valueOf(5000);
    private static final BigDecimal RECEIVE_AMOUNT = BigDecimal.valueOf(2000);

    @Test
    @DisplayName("뿌리기 성공")
    public void sprinkleTest() throws Exception {
        // given
        sprinkle();

        // when
        mockMvc.perform(
                post("/api/v2/sprinkle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-ROOM-ID", SPRINKLE_USER_ID)
                        .header("X-USER-ID", ROOM_ID)
                        .content(mapper.writeValueAsString(sprinkleRequest)))
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.roomId").value(ROOM_ID))
                .andExpect(jsonPath("$.data.userId").value(SPRINKLE_USER_ID))
                .andExpect(jsonPath("$.data.token", hasLength(3)));
    }

    @Test
    @DisplayName("받기 성공")
    public void receiveTest() throws Exception {
        // given
        receive();

        // when
        mockMvc.perform(
                patch("/api/v2/sprinkle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", RECEIVE_USER_ID)
                        .header("X-ROOM-ID", ROOM_ID)
                        .header("X-TOKEN", TOKEN))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.userId").value(RECEIVE_USER_ID))
                .andExpect(jsonPath("$.data.amount").value(RECEIVE_AMOUNT));
    }

    @Test
    @DisplayName("조회 성공")
    public void inquiryTest() throws Exception {
        // given
        inquiry();

        // when
        mockMvc.perform(
                get("/api/v2/sprinkle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", SPRINKLE_USER_ID)
                        .header("X-ROOM-ID", ROOM_ID)
                        .header("X-TOKEN", TOKEN))
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.sprinkledAmount").value(SPRINKLE_AMOUNT))
                .andExpect(jsonPath("$.data.receivedAmount").value(RECEIVE_AMOUNT))
                .andExpect(jsonPath("$.data.receivedInfo[0].userId").value(RECEIVE_USER_ID))
                .andExpect(jsonPath("$.data.receivedInfo[0].amount").value(RECEIVE_AMOUNT))
        ;
    }

    private void sprinkle() {
        sprinkleRequest = new SprinkleRequest(BigDecimal.valueOf(5000), 3);
        SprinkleResponse sprinkleResponse = SprinkleResponse.builder().userId(SPRINKLE_USER_ID).roomId(ROOM_ID).token(TOKEN).build();

        given(sprinkleService.newSprinkle(anyLong(), anyLong(), Mockito.any(SprinkleRequest.class))).willReturn(sprinkleResponse);
    }

    private void receive() {
        ReceiveResponse receiveResponse = ReceiveResponse.builder().userId(RECEIVE_USER_ID).amount(RECEIVE_AMOUNT).build();

        given(sprinkleService.receive(RECEIVE_USER_ID, ROOM_ID, TOKEN)).willReturn(receiveResponse);
    }

    private void inquiry() {
        ReceiveResponse receiveResponse = ReceiveResponse.builder().userId(RECEIVE_USER_ID).amount(RECEIVE_AMOUNT).build();
        List<ReceiveResponse> receiveResponseList = new ArrayList<>();
        receiveResponseList.add(receiveResponse);

        InquiryResponse inquiryResponse = InquiryResponse.builder().sprinkledAmount(SPRINKLE_AMOUNT).receivedAmount(RECEIVE_AMOUNT).receivedInfo(receiveResponseList).build();
        given(sprinkleService.inquiry(SPRINKLE_USER_ID, ROOM_ID, TOKEN)).willReturn(inquiryResponse);
    }
}