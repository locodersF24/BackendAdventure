package org.example.backendadventure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.backendadventure.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class AvailabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Relevant for POST and PUT Reservation.
    // Check this in ReservationService right after saving the reservation.
    public Boolean isNumberOfPeopleIsTooHigh(Reservation reservation) {
        return entityManager.createQuery("""
                                SELECT SUM(r.numberOfPeople) > :maxNumberOfPeople
                                FROM Reservation r
                                WHERE r.id != :id AND r.date = :date AND r.timeSlot.id = :timeSlotId""",
                        Boolean.class)
                .setParameter("maxNumberOfPeople", reservation.getTimeSlot().getActivity().getMaxNumberOfPeople())
                .setParameter("id", reservation.getId())
                .setParameter("date", reservation.getDate())
                .setParameter("timeSlotId", reservation.getTimeSlot().getId())
                .getSingleResult();
    }

    // Returns map where key is TimeSlot.id and value is sum of numberOfPeople based on Reservation date.
    public Map<Integer, Long> timeSlotIdToAvailable(LocalDate date) {
        Query query = entityManager.createQuery("""
                        SELECT t.id, SUM(r.numberOfPeople)
                        FROM TimeSlot t
                        LEFT JOIN Reservation r ON t.id = r.timeSlot.id
                        WHERE r.date = :date
                        GROUP BY t.id""")
                .setParameter("date", date);
        List<Object[]> results = query.getResultList();
        Map<Integer, Long> map = new HashMap<>();
        for (Object[] result : results) {
            Integer timeSlotId = (Integer) result[0];
            Long sumOfPeople = (Long) result[1];
            map.put(timeSlotId, sumOfPeople);
        }
        return map;
    }

}
