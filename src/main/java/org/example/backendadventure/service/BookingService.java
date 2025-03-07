package org.example.backendadventure.service;

import org.example.backendadventure.model.Booking;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final ReservationRepository reservationRepository;

    public BookingService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Booking> readAllBookings() {
        return reservationRepository
            .findAll()
            .stream()
            .map(Reservation::toBooking)
            .toList();
    }

}
