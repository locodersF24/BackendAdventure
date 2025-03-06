package org.example.backendadventure.service;

import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Optional<Reservation> findById(int id){
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void delete(Reservation reservation) {
        reservationRepository.deleteById(reservation.getId());
    }




}
