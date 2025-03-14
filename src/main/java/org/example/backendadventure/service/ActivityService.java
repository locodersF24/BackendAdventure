package org.example.backendadventure.service;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.TimeSlot;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final AvailabilityRepository availabilityRepository;

    public ActivityService(ActivityRepository activityRepository, AvailabilityRepository availabilityRepository) {
        this.activityRepository = activityRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<Activity> getActivities() {
        return activityRepository.findAll();
    }

    public List<TimeSlot> getTimeSlots(int activityId, LocalDate date) {

        // Not found
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (activity.isEmpty()) return new ArrayList<>();

        // No date
        if (date == null) return activity.get().getTimeSlots();

        // With availability
        return availabilityRepository.timeSlotsWithAvailable(activity.get(), date);
    }

}
