package kakaopay.sprinkle.controller;

import kakaopay.sprinkle.service.SprinkleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SprinkleController.class)
@AutoConfigureMockMvc
class SprinkleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SprinkleService sprinkleService;

    @Test
    public void sprinkleTest() {

    }

}