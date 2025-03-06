package org.example.backendadventure.controller;

import org.example.backendadventure.model.Booking;
import org.example.backendadventure.service.BookingService;
import org.example.backendadventure.service.InitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class BookingOverviewController {

    private final InitService initService;
    private final BookingService bookingService;

    public BookingOverviewController(InitService initService, BookingService bookingService) {
        this.initService = initService;
        this.bookingService = bookingService;
    }

    @GetMapping("/init")
    public ResponseEntity initData() {
        initService.initData();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> findAll() {
        List<Booking> allReservations = bookingService.readAllBookings();
        return new ResponseEntity<>(allReservations, HttpStatus.OK);
    }

}
