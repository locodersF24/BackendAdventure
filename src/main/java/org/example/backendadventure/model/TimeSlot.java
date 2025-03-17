package org.example.backendadventure.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer availableNumberOfPeople;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Integer getAvailableNumberOfPeople() {
        return availableNumberOfPeople;
    }

    public void setAvailableNumberOfPeople(Integer availableNumberOfPeople) {
        this.availableNumberOfPeople = availableNumberOfPeople;
    }
}
