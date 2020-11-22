package kakaopay.sprinkle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaopay.sprinkle.dto.SprinkleRequest;
import kakaopay.sprinkle.service.SprinkleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private static final String token = "abc";

    @Test
    public void sprinkleTest() throws Exception {

        // given
        sprinkle();

        // when
        mockMvc.perform(
                post("/api/v2/sprinkle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-ROOM-ID", 1)
                        .header("X-USER-ID", 1)
                        .content(mapper.writeValueAsString(sprinkleRequest)))
                // then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data", hasLength(3)))
        ;
    }

//    @Test
//    public void receiveTest() throws Exception {
//
//        // when
//        mockMvc.perform(
//                post("/api/v2/sprinkle")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("X-ROOM-ID", 1)
//                        .header("X-USER-ID", 1)
//                        .header("TOKEN", ""))
//                // then
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value("0000"))
////                .andExpect(jsonPath("$.data", hasLength(3)))
//        ;
//
//    }

    private void sprinkle() {

        sprinkleRequest = new SprinkleRequest(5000, 3);

        given(sprinkleService.newSprinkle(anyLong(), anyLong(), Mockito.any(SprinkleRequest.class))).willReturn(token);

    }

}