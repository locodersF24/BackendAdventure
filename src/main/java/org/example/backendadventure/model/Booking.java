package org.example.backendadventure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

// DTO: data transfer object
public record Booking(
        @Id int reservationId,
        String activity, // Activity name
        int numberOfPeople,
        LocalDate date,
        int timeSlotCode,
        // Rest from contact person
        String firstName,
        String lastName,
        String phoneNumber,
        String email
) {}