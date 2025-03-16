package org.example.backendadventure.controller;
import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.service.ActivityService;
import org.example.backendadventure.service.EquipmentService;
import org.example.backendadventure.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class ReservationRestController {

    ReservationService reservationService;
    ActivityService activityService;
    EquipmentService equipmentService;


    public ReservationRestController(ReservationService reservationService, ActivityService activityService, EquipmentService equipmentService) {
        this.reservationService = reservationService;
        this.activityService = activityService;
        this.equipmentService = equipmentService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<Map<String, Object>> createReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.createReservation(reservation);
        // Hent datoen fra reservationen eller brug den aktuelle dato
        LocalDate date = reservation.getDate() != null ? reservation.getDate() : LocalDate.now();

        Map<String, String> updatedAvailability = reservationService.updateAvailability(reservation.getActivity().getName(),reservation.getDate(),reservation.getNumberOfPeople());

        // returnér både reservationen og den nye availability
        Map<String, Object> response = new HashMap<>();
        response.put("reservation", savedReservation);
        response.put("updatedAvailability", updatedAvailability);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @GetMapping("/activities")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        if (activities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activities);
    }

}
