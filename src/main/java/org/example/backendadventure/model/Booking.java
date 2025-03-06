package org.example.backendadventure.model;

import java.time.LocalDate;

public record Booking(int numberOfPeople,
                      LocalDate date,
                      String timeInterval, // From time slot code
                      String activity, // Activity name
                      // Rest from contact person
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      String email) {}