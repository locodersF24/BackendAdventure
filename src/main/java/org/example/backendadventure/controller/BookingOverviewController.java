package org.example.backendadventure.controller;

import org.example.backendadventure.model.Booking;
import org.example.backendadventure.service.BookingService;
import org.example.backendadventure.service.DummyDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Booking>> search(@RequestParam Map<String, String> searchParams) {
        try {
            List<Booking> bookings = bookingService.searchBookings(searchParams);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
