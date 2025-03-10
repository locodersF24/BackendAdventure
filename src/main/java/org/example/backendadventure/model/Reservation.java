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

    public Reservation() {}

    public Reservation(int numberOfPeople, LocalDate date, int timeSlotCode, Activity activity, ContactPerson contactPerson) {
        this.numberOfPeople = numberOfPeople;
        this.date = date;
        this.timeSlotCode = timeSlotCode;
        this.activity = activity;
        this.contactPerson = contactPerson;
    }

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

    public Booking toBooking() {

        String timeInterval = switch (timeSlotCode) {
            case 1 -> "08:00-09:00";
            case 2 -> "09:00-10:00";
            case 3 -> "10:00-11:00";
            case 4 -> "11:00-12:00";
            case 5 -> "12:00-13:00";
            case 6 -> "13:00-14:00";
            case 7 -> "14:00-15:00";
            case 8 -> "15:00-16:00";
            case 9 -> "16:00-17:00";
            default -> "Unknown";
        };

        return new Booking(id,
                activity.getName(),
                numberOfPeople,
                date,
                timeInterval,
                contactPerson.getFirstName(),
                contactPerson.getLastName(),
                contactPerson.getPhoneNumber(),
                contactPerson.getEmail());
    }
}
