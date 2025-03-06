package org.example.backendadventure.service;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }

    public List<Reservation> getAvailability(String activity, LocalDate date) {
        return reservationRepository.findAvailability(activity, date); //noget ala det her
    }
}
