package org.example.backendadventure.controller;

import org.example.backendadventure.model.Booking;
import org.example.backendadventure.service.BookingService;
import org.example.backendadventure.service.DummyDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin // Important for JavaScript
@RestController
public class BookingOverviewController {

    private final DummyDataService dummyDataService;
    private final BookingService bookingService;

    public BookingOverviewController(DummyDataService dummyDataService, BookingService bookingService) {
        this.dummyDataService = dummyDataService;
        this.bookingService = bookingService;
    }

    @GetMapping("/init")
    public ResponseEntity initDummyData() {
        dummyDataService.init();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> readAll() {
        List<Booking> allBookings = bookingService.readAllBookings();
        return new ResponseEntity<>(allBookings, HttpStatus.OK);
    }

}
