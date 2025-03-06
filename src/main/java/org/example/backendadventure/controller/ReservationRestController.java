package org.example.backendadventure.controller;

import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class ReservationRestController {

    ReservationService reservationService;

    @PutMapping("reservation/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable int id, @RequestBody Reservation reservation) {
        if(id != reservation.getId()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (reservationService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(reservationService.save(reservation), HttpStatus.OK);
    }

    @DeleteMapping("reservation/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        reservationService.delete(reservation.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
