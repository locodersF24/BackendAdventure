package org.example.backendadventure.controller;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.TimeSlot;
import org.example.backendadventure.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
public class ActivityRestController {

    private final ActivityService activityService;

    public ActivityRestController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public ResponseEntity<List<Activity>> getActivities() {
        return ResponseEntity.ok(activityService.getActivities());
    }

    @GetMapping("/activities/{id}/timeslots")
    public ResponseEntity<List<TimeSlot>> getTimeSlots(@PathVariable int id, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(activityService.getTimeSlots(id, date));
    }

}
