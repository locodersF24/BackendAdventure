package org.example.backendadventure.repository;

import org.example.backendadventure.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByDateAndActivity_id(LocalDate date, int activityId);

    //

    // Find reservationer baseret på aktivitet og dato
    @Query("SELECT r FROM Reservation r WHERE r.activity.name = :activity AND r.date = :date")
    List<Reservation> findAvailability(@Param("activity") String activity, @Param("date") LocalDate date);

}
