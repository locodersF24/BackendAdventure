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
        Thread.sleep(1000);
        generateKeySmashReservations(10, 1); // Creates 10 reservations over 1 days (first day is today).
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
        List<String> names = List.of("Go-kart", "Minigolf", "Paintball", "Sumo Wrestling");
        for (String name : names) {
            Activity activity = new Activity();
            activity.setName(name);
            activity.setMaxNumberOfPeople(10);
            activity.setAgeLimit(10);
            activityRepository.save(activity);

            // Making time slots: "08:00-09:00" to "15:00-16:00"
            // Note: Time slots can differ based on activity, but here they are similar.
            for (int j = 0; j < 8; j++) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(LocalTime.of(8 + j, 0));
                timeSlot.setEndTime(LocalTime.of(9 + j, 0));
                timeSlot.setActivity(activity);
                timeSlotRepository.save(timeSlot);
            }
        }

    }
    
    private void generateKeySmashReservations(int amount, int days) {
        List<Activity> activities = activityRepository.findAll();
        for (int i = 0; i < amount; i++) {
            Activity activity = activities.get(random.nextInt(activities.size()));
            List<TimeSlot> timeSlots = activity.getTimeSlots();

            ContactPerson contactPerson = new ContactPerson();
            contactPerson.setFirstName(randomLetters(5));
            contactPerson.setLastName(randomLetters(5));
            contactPerson.setPhoneNumber(randomNumbers(8));
            contactPerson.setEmail(randomLetters(5) + "@" + randomLetters(5) + ".dk");
            contactPersonRepository.save(contactPerson);

            Reservation reservation = new Reservation();
            reservation.setContactPerson(contactPerson);
            reservation.setActivity(activity);
            reservation.setTimeSlot(timeSlots.get(random.nextInt(timeSlots.size())));
            reservation.setDate(LocalDate.now().plusDays(random.nextInt(days)));
            reservation.setNumberOfPeople(random.nextInt(9) + 1); // 1-9 people
            reservationRepository.save(reservation);
        }
    }

    // Auxiliary methods
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
