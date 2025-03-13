package org.example.backendadventure.service;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.model.TimeSlot;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ReservationRepository reservationRepository;

    public ActivityService(ActivityRepository activityRepository, ReservationRepository reservationRepository) {
        this.activityRepository = activityRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Activity> getActivities() {
        return activityRepository.findAll();
    }

    public List<TimeSlot> getTimeSlots(int activityId, LocalDate date) {

        // Not found
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) return new ArrayList<>();

        // No date
        List<TimeSlot> timeSlots = activity.get().getTimeSlots();
        if (date == null) return timeSlots;

        // With available
        Map<Integer, Integer> timeSlotIdToSum = new HashMap<>();
        timeSlots.forEach(timeSlot -> timeSlotIdToSum.put(timeSlot.getId(), 0));
        List<Reservation> reservations = reservationRepository.findByDateAndActivity_id(date, activityId);
        for (Reservation reservation : reservations) {
            timeSlotIdToSum.computeIfPresent(
                    reservation.getTimeSlot().getId(),
                    (k, v) -> v + reservation.getNumberOfPeople()
            );
        }
        timeSlots.forEach(timeSlot -> {
            timeSlot.setAvailable(activity.get().getMaxNumberOfPeople() - timeSlotIdToSum.get(timeSlot.getId()));
        });
        return timeSlots;
    }

}
