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
import java.util.NoSuchElementException;

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
    public ResponseEntity<Booking> initDummyData() {
        dummyDataService.init();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> search(@RequestParam Map<String, String> searchParams) {
        try {
            return new ResponseEntity<>(bookingService.searchBookings(searchParams), HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> findById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<Booking> update(@PathVariable int id, @RequestBody Booking booking) {
        if (id != booking.reservationId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!bookingService.updateBooking(booking)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
