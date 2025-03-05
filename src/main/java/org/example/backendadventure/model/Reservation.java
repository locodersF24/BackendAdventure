package org.example.backendadventure.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerates id
    private int id;
    @Column(nullable = false)
    private int numberOfPeople;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private int timeSlotCode;

    @ManyToOne
    @JoinColumn(name = "activity", referencedColumnName = "id")
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "contactPerson", referencedColumnName = "id")
    private ContactPerson contactPerson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTimeSlotCode() {
        return timeSlotCode;
    }

    public void setTimeSlotCode(int timeSlotCode) {
        this.timeSlotCode = timeSlotCode;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }
}
