package org.example.backendadventure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.model.TimeSlot;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class AvailabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Relevant for POST and PUT Reservation.
    // Check this in ReservationService right before saving the reservation.
    public Boolean isNumberOfPeopleIsTooHigh(Reservation reservation) {
        return entityManager.createQuery("""
                                SELECT SUM(r.numberOfPeople) >= :maxNumberOfPeople
                                FROM Reservation r
                                WHERE r.id != :id AND r.date = :date AND r.timeSlot.id = :timeSlotId""",
                        Boolean.class)
                .setParameter("maxNumberOfPeople", reservation.getActivity().getMaxNumberOfPeople())
                .setParameter("id", reservation.getId())
                .setParameter("date", reservation.getDate()) // toString() ????
                .setParameter("timeSlotId", reservation.getTimeSlot().getId())
                .getSingleResult();
    }

    // Relevant for GET Activity.timeSlots by Reservation.date.
    // Returns timeSlots from an Activity with available (number) based on Reservation date.
    public List<TimeSlot> timeSlotsWithAvailable(Activity activity, LocalDate date) {
        Query query = entityManager.createQuery("""
                        SELECT t.id, SUM(r.numberOfPeople)
                        FROM TimeSlot t
                        LEFT JOIN Reservation r ON t.id = r.timeSlot.id
                        WHERE r.date = :date AND r.activity.id = :activityId
                        GROUP BY t.id""")
                .setParameter("date", date)
                .setParameter("activityId", activity.getId());
        Map<Integer, TimeSlot> timeSlotMap = new HashMap<>();
        for (TimeSlot timeSlot : activity.getTimeSlots()) {
            timeSlot.setAvailable(activity.getMaxNumberOfPeople());
            timeSlotMap.put(timeSlot.getId(), timeSlot);
        }
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Integer timeSlotId = (Integer) result[0];
            Long sumOfPeople = (Long) result[1];
            int available = activity.getMaxNumberOfPeople() - sumOfPeople.intValue();
            timeSlotMap.get(timeSlotId).setAvailable(available);
        }
        return new ArrayList<>(timeSlotMap.values());
    }

}
