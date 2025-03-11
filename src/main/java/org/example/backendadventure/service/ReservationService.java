package org.example.backendadventure.service;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Map<String, String> getAvailability(String activity, LocalDate date, int numberOfPeople) {
        List<Reservation> reservations = reservationRepository.findAvailability(activity, date);

        List<LocalTime> availableTime = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        Map<String, String> availabilityMap = new HashMap<>();
        int maxNumberOfPeople = 20;

        for (LocalTime availableSlot : availableTime) {
            int bookedSpots = reservations.stream()//en stream af reservationer
                    .filter(r -> convertTimeToSlot(availableSlot) == r.getTimeSlotCode())//sammenligner tidskode med timeslot fra r (reservationen)
                    .mapToInt(Reservation::getNumberOfPeople) //henter numberofpeople for hver reservation
                    .sum(); //summerer mængden af numberofpeople

            if (bookedSpots == 0) {
                availabilityMap.put(availableSlot.toString(), "available");
            } else if (bookedSpots > 0 && bookedSpots < maxNumberOfPeople) {
                availabilityMap.put(availableSlot.toString(), "limited availability");
            } else {
                availabilityMap.put(availableSlot.toString(), "unavailable");
            }
        }
        return availabilityMap;
    }

    private int convertTimeToSlot(LocalTime time) {
        // Returner kun timerne i tidskoden
        return time.getHour() * 100 + time.getMinute();  // Dvs. 08:00 bliver 800, 08:30 bliver 830
    }

}
