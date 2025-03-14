package org.example.backendadventure.controller;

import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.model.ReservationDTO;
import org.example.backendadventure.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> search(@RequestParam Map<String, String> searchParams) {
        return ResponseEntity.ok(reservationService.searchReservations(searchParams));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getOne(@PathVariable int id) {
        Optional<Reservation> reservation = reservationService.getOne(id);
        if (reservation.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reservation.get());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (!reservationService.delete(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody ReservationDTO reservationDTO) {
        if (id != reservationDTO.reservationId()) return ResponseEntity.badRequest().build();
        Boolean b = reservationService.updateFromDTO(reservationDTO);
        if (b == null) return ResponseEntity.notFound().build();
        if (!b) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Integer> create(@RequestBody ReservationDTO reservationDTO) {
        int id = reservationService.createFromDTO(reservationDTO);
        if (id == -1) return ResponseEntity.badRequest().build();
        if (id == -2) ResponseEntity.status(HttpStatus.CONFLICT).build();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    //

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
