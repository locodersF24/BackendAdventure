package org.example.backendadventure;

import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationRestControllerTest.class)
public class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setup() {
        reservation = new Reservation();
    }


}
