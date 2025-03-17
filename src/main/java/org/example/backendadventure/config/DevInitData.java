package org.example.backendadventure.config;

import org.example.backendadventure.model.*;
import org.example.backendadventure.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@Profile("dev") // Restricts the component to the dev Spring profile.
public class DevInitData implements CommandLineRunner {

    // Data is initialized in this method.
    @Override
    public void run(String... args) throws InterruptedException {
        setupLoginProfiles();
        setupActivities();
        generateKeySmashReservations(30, 1); // Creates 30 reservations over 1 days (first day is today).
    }

    // Attributes
    private final Random random;
    private final ActivityRepository activityRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final LoginProfileRepository loginProfileRepository;
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    // Constructor
    public DevInitData(ActivityRepository activityRepository,
                       ContactPersonRepository contactPersonRepository,
                       LoginProfileRepository loginProfileRepository,
                       ReservationRepository reservationRepository,
                       TimeSlotRepository timeSlotRepository) {
        this.random = new Random();
        this.activityRepository = activityRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.loginProfileRepository = loginProfileRepository;
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    // Methods for run
    private void setupLoginProfiles() {
        LoginProfile reservationManager = new LoginProfile();
        LoginProfile activityManager = new LoginProfile();

        reservationManager.setUsername("Reservation-Manager");
        reservationManager.setPassword("Password123");

        activityManager.setUsername("Activity-Manager");
        activityManager.setPassword("Password123");

        loginProfileRepository.save(reservationManager);
        loginProfileRepository.save(activityManager);
    }

    private void setupActivities() {

        // Making activities
        List<String> names = List.of("Climbing", "Go-kart", "Minigolf", "Sumo Wrestling");
        List<Integer> maxes = List.of(6, 8, 4, 2);
        List<Integer> minutes = List.of(60, 30, 90, 45);
        List<Integer> ages = List.of(10, 10, 10, 10);
        for (int i = 0; i < 4; i++) {
            Activity activity = new Activity();
            activity.setName(names.get(i));
            activity.setMaxNumberOfPeople(maxes.get(i));
            activity.setAgeLimit(ages.get(i));
            activityRepository.save(activity);

            // Making time slots
            int startTime = 10 * 60; // 10:00
            int endTimeOfDay = 16 * 60; // 16:00
            for (int j = 0; j < 8; j++) {
                startTime += minutes.get(i) * j;
                int endTimeOfActivity = startTime + minutes.get(i);
                if (endTimeOfActivity > endTimeOfDay) break;
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(timeFromMinutes(startTime));
                timeSlot.setEndTime(timeFromMinutes(endTimeOfActivity));
                timeSlot.setActivity(activity);
                timeSlotRepository.save(timeSlot);
            }
        }

    }
    
    private void generateKeySmashReservations(int amount, int days) {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        for (int i = 0; i < amount; i++) {
            ContactPerson contactPerson = new ContactPerson();
            contactPerson.setFirstName(randomLetters(5));
            contactPerson.setLastName(randomLetters(5));
            contactPerson.setPhoneNumber(randomNumbers(8));
            contactPerson.setEmail(randomLetters(5) + "@" + randomLetters(5) + ".dk");
            contactPersonRepository.save(contactPerson);

            Reservation reservation = new Reservation();
            reservation.setContactPerson(contactPerson);
            reservation.setTimeSlot(timeSlots.get(random.nextInt(timeSlots.size())));
            reservation.setDate(LocalDate.now().plusDays(random.nextInt(days)));
            reservation.setNumberOfPeople(random.nextInt(9) + 1); // 1-9 people
            reservationRepository.save(reservation);
        }
    }

    // Auxiliary methods
    private LocalTime timeFromMinutes(int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;
        return LocalTime.of(hour, minute);
    }

    private String randomLetters(int length) {
        List<Character> letters = List.of('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < length; j++) {
            s.append(letters.get(random.nextInt(letters.size())));
        }
        return s.toString();
    }

    private String randomNumbers(int length) {
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < length; j++) {
            s.append(random.nextInt(10));
        }
        return s.toString();
    }

}
