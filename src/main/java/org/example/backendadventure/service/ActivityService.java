package org.example.backendadventure.service;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.TimeSlot;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.AvailabilityRepository;
import org.example.backendadventure.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final AvailabilityRepository availabilityRepository;
    private final TimeSlotRepository timeSlotRepository;

    public ActivityService(ActivityRepository activityRepository,
                           AvailabilityRepository availabilityRepository,
                           TimeSlotRepository timeSlotRepository) {
        this.activityRepository = activityRepository;
        this.availabilityRepository = availabilityRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<Activity> getActivities() {
        return activityRepository.findAll();
    }

    public List<TimeSlot> getTimeSlotsWithAvailability(LocalDate date) {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        Map<Integer, Long> idToSum = availabilityRepository.timeSlotIdToAvailable(date);
        timeSlots.forEach(timeSlot -> {
            Long value = idToSum.get(timeSlot.getId());
            int sum = value == null ? 0 : value.intValue();
            int max = timeSlot.getActivity().getMaxNumberOfPeople();
            timeSlot.setAvailableNumberOfPeople(max - sum);
        });
        return timeSlots;
    }

}
