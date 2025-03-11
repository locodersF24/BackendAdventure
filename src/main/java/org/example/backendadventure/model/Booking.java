package org.example.backendadventure.model;

import java.time.LocalDate;

// DTO: data transfer object
public record Booking(int reservationId,
                      String activity, // Activity name
                      int numberOfPeople,
                      LocalDate date,
                      String timeInterval, // From time slot code
                      // Rest from contact person
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      String email) {}