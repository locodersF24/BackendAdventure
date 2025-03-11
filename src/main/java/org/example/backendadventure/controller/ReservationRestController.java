package org.example.backendadventure.controller;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.example.backendadventure.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class ReservationRestController {

    ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String,String>> getAvailability(
            @RequestParam String activity,
            @RequestParam String date,
            @RequestParam int numberOfPeople) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Map<String, String> availability = reservationService.getAvailability(
                    activity,
                    localDate,
                    numberOfPeople
            );
            return ResponseEntity.ok(availability);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}

// Dummy data for tilgængelighed
//        Map<String, String> availability = new HashMap<>();
//        availability.put("10:00", "Available");
//        availability.put("12:00", "Fully Booked");
//        availability.put("14:00", "Available");
//        availability.put("16:00", "Limited Spots");
