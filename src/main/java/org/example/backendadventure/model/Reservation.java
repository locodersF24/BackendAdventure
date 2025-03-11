package org.example.backendadventure.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    public boolean changeByBooking(Booking booking, List<Activity> allActivities) {

        // No such Activity?
        if (!allActivities
                .stream()
                .map(Activity::getName)
                .toList()
                .contains(booking.activity())) {
            return false;
        }

        // Reservation attributes
        numberOfPeople = booking.numberOfPeople();
        date = booking.date();
        timeSlotCode = booking.timeSlotCode();

        // ContactPerson attributes
        if (contactPerson == null) {
            contactPerson = new ContactPerson();
        }
        contactPerson.setFirstName(booking.firstName());
        contactPerson.setLastName(booking.lastName());
        contactPerson.setPhoneNumber(booking.phoneNumber());
        contactPerson.setEmail(booking.email());

        // Activity
        for (Activity activity : allActivities) {
            if (activity.getName().equals(booking.activity())) {
                this.activity = activity;
                break;
            }
        }
        return true;
    }

    public Booking toBooking() {
        return new Booking(
                id,
                activity.getName(),
                numberOfPeople,
                date,
                timeSlotCode,
                contactPerson.getFirstName(),
                contactPerson.getLastName(),
                contactPerson.getPhoneNumber(),
                contactPerson.getEmail()
        );
    }
}
