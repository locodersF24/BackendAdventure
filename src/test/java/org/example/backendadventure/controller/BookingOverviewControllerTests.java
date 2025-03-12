package org.example.backendadventure.controller;

import org.example.backendadventure.model.Booking;
import org.example.backendadventure.service.BookingService;
import org.example.backendadventure.service.DummyDataService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingOverviewController.class)
public class BookingOverviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private DummyDataService dummyDataService;

    private List<Booking> bookings;
    private LocalDate localDate;
    private LocalDate localDate2;
    private Map<String, String> searchParams;

    @BeforeEach
    void setUp() {
        localDate = LocalDate.parse("2025-03-12");
        localDate2 = LocalDate.parse("2025-12-12");
        bookings = new ArrayList<>();
        bookings.add(new Booking(1, "Sumo", 1, localDate, 1, "Jens", "Hansen", "12345678", "test@test.dk"));
        bookings.add(new Booking(2, "Gokart", 2, localDate2, 2, "John", "Doe", "87654321", "test2@test.dk"));
        bookings.add(new Booking(3, "Minigolf", 3, localDate2, 3, "Jane", "Doe", "76543218", "test3@test.dk"));
        searchParams = new HashMap<>();
    }

    @Test
    void searchAllBookings() throws Exception {
        when(bookingService.searchBookings(searchParams)).thenReturn(bookings);
        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(bookings.size())));
    }

    @Test
    void searchByFirstNameLastName() throws Exception {
        //Arrange
        when(bookingService.searchBookings(searchParams)).thenReturn(List.of(bookings.getFirst()));
        searchParams.put("firstName", "Jens");
        searchParams.put("lastName", "Hansen");

        //Act
        mockMvc.perform(get("/bookings")
                .param("firstName", "Jens")
                .param("lastName", "Hansen"))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", Matchers.equalTo("Jens")))
                .andExpect(jsonPath("$[0].lastName", Matchers.equalTo("Hansen")));
    }

    @Test
    void searchByFirstNamePhoneNumber() throws Exception {
        //Arrange
        when(bookingService.searchBookings(searchParams)).thenReturn(List.of(bookings.getFirst()));
        searchParams.put("firstName", "Jens");
        searchParams.put("phoneNumber", "12345678");

        //Act
        mockMvc.perform(get("/bookings")
                .param("firstName", "Jens")
                .param("phoneNumber", "12345678"))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", Matchers.equalTo("Jens")))
                .andExpect(jsonPath("$[0].phoneNumber", Matchers.equalTo("12345678")));
    }

    @Test
    void searchByPhoneNumberEmail() throws Exception {
        //Arrange
        when(bookingService.searchBookings(searchParams)).thenReturn(List.of(bookings.getFirst()));
        searchParams.put("phoneNumber", "12345678");
        searchParams.put("email", "test@test.dk");

        //Act
        mockMvc.perform(get("/bookings")
                        .param("phoneNumber", "12345678")
                        .param("email", "test@test.dk"))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].phoneNumber", Matchers.equalTo("12345678")))
                .andExpect(jsonPath("$[0].email", Matchers.equalTo("test@test.dk")));
    }

    @Test
    void searchByActivityPhoneNumber() throws Exception {
        //Arrange
        when(bookingService.searchBookings(searchParams)).thenReturn(List.of(bookings.getFirst()));
        searchParams.put("activity", "Sumo");
        searchParams.put("phoneNumber", "12345678");

        //Act
        mockMvc.perform(get("/bookings")
                        .param("activity", "Sumo")
                        .param("phoneNumber", "12345678"))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].activity", Matchers.equalTo("Sumo")))
                .andExpect(jsonPath("$[0].phoneNumber", Matchers.equalTo("12345678")));
    }

    @Test
    void searchByDatePhoneNumber() throws Exception {
        //Arrange
        when(bookingService.searchBookings(searchParams)).thenReturn(List.of(bookings.getFirst()));
        searchParams.put("date", "2025-03-12");
        searchParams.put("phoneNumber", "12345678");

        //Act
        mockMvc.perform(get("/bookings")
                        .param("date", "2025-03-12")
                        .param("phoneNumber", "12345678"))

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].date", Matchers.equalTo("2025-03-12")))
                .andExpect(jsonPath("$[0].phoneNumber", Matchers.equalTo("12345678")));
    }

    @Test
    void updateBooking() throws Exception {
        //Arrange
        when(bookingService.updateBooking(bookings.getFirst())).thenReturn(true);

        //Act
        mockMvc.perform(put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookings.getFirst().toJSONString()))

        //Assert
                .andExpect(status().isOk());
    }

}
