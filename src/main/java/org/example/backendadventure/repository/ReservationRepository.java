package org.example.backendadventure.repository;

import org.example.backendadventure.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r JOIN r.activity a WHERE a.name = :activityName AND r.date = :date")
    List<Reservation> findAvailability(@Param("activityName") String activityName, @Param("date") LocalDate date);
}

