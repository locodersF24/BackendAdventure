//package org.example.backendadventure.controller;
//
//import org.example.backendadventure.model.Activity;
//import org.example.backendadventure.model.Reservation;
//import org.example.backendadventure.repository.ReservationRepository;
//import org.example.backendadventure.service.ReservationService;
//import org.springframework.cglib.core.Local;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class ReservationRestController {
//
//    ReservationService reservationService;
//    ReservationRepository reservationRepository;
//
//    public ReservationRestController(ReservationService reservationService) {
//        this.reservationService = reservationService;
//    }
//
//    @PostMapping("/reservation")
//    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
//        if (reservationRepository.existsById(reservation.getId())) {
//            return new ResponseEntity<>(reservation, HttpStatus.CONFLICT);
//        }
//        return new ResponseEntity<>(reservationRepository.save(reservation), HttpStatus.CREATED);
//    }
//
//
//    @GetMapping("/availability")
//    public ResponseEntity<Map<String,String>> getAvailability(
//            @RequestParam LocalDate date,
//            @RequestParam int numberOfPeople,
//            @RequestParam String activity) {
//
//
//        LocalDate selectedDate = LocalDate.parse(date.toString());
//        List<Reservation> reservations = reservationService.getAvailability(activity,selectedDate);
//
//        List<LocalTime> availableTime = List.of(
//                LocalTime.of(8,0),
//                LocalTime.of(9,0)); //noget ala det her - en liste med availaletime på den selected date
//
////        Map<String, String> availabilityMap = new HashMap<>(); //skal somehow tjekke bookedspots for selectedDate
////        for(LocalTime time : availableTime) {
////            int bookedSpots = 0;
////            for (Reservation reservation : reservations) {
////                if (bookedSpots == 0) {
////                    availabilityMap.put(time.toString(),"available");
////                    //if bookedspots er over 1 men under capacity så returner limited
////                } else if (bookedSpots >0 && reservation.getActivity().((!equals(activity.maxNumberOfPeople)));
////                    availabilityMap.put(time.toString(),"still some spots left");
////            } else {
////                    availabilityMap.put(time.toString(),"unavailable");
////                }
////            }
////        return ResponseEntity.ok(availabilityMap);
////
////    }
//
//
//        //backend sender json objekt med dato og reservation med de forbehold der er for reservation.
//        // for hver timeslot er det så available or not
//        //json objekt der har array eller map med timeslot - backend er information expert.
//        //når booket så genbrug koden
//
////    }
//
//}
