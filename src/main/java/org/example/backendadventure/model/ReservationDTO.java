package org.example.backendadventure.model;

import java.time.LocalDate;

// Data transfer object (DTO) for receiving data from an html form.
public record ReservationDTO(
        // Reservation
        int reservationId, // NOTE: 0 for POST, but the actual value for PUT.
        int numberOfPeople,
        LocalDate date,
        // TimeSlot
        int timeSlotId,
        // Activity
        int activityId, // NOTE: Check if timeslot.activity.id matches this.
        // ContactPerson
        int contactPersonId, // NOTE: 0 for POST, but for PUT check if reservation.contactPerson.id matches this.
        String firstName,
        String lastName,
        String phoneNumber,
        String email
) {

    public void update(ContactPerson contactPerson) {
        contactPerson.setFirstName(this.firstName());
        contactPerson.setLastName(this.lastName());
        contactPerson.setPhoneNumber(this.phoneNumber());
        contactPerson.setEmail(this.email());
    }

    public void update(Reservation reservation) {
        reservation.setNumberOfPeople(this.numberOfPeople());
        reservation.setDate(this.date);
    }

    public String toJSONString() {
        return this.toString()
                .substring(8)
                .replace("[", "{\"")
                .replace("=", "\" : \"")
                .replace(", ", "\",\"")
                .replace("]", "\"}");
    }

}