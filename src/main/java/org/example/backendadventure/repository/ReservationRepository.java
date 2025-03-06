package org.example.backendadventure.repository;

import org.example.backendadventure.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findAvailability(String activity, LocalDate date);
}
