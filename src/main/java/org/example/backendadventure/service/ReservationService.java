package org.example.backendadventure.service;

import org.example.backendadventure.model.*;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.ContactPersonRepository;
import org.example.backendadventure.repository.ReservationRepository;
import org.example.backendadventure.repository.TimeSlotRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ActivityRepository activityRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ContactPersonRepository contactPersonRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ActivityRepository activityRepository,
                              ContactPersonRepository contactPersonRepository,
                              TimeSlotRepository timeSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.activityRepository = activityRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    private List<String> getPaths(Field[] fields, String prefix) {
        System.out.print("Include in search: ");
        List<String> attributes = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getType().equals(String.class) && !field.getType().equals(LocalDate.class)) {
                attributes.add(prefix + field.getName());
            } else {
                System.out.print(field.getName() + ", ");
            }
        }
        System.out.print("\nExclude in search: ");
        attributes.forEach(System.out::print);
        System.out.println();
        return attributes;
    }

    public List<Reservation> searchReservations(Map<String, String> searchParams) {

        if (searchParams.isEmpty()) return reservationRepository.findAll();

        System.out.println(searchParams);

        // Making the probe
        Activity activity = new Activity();
        activity.setName(searchParams.get("activity"));
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setFirstName(searchParams.get("firstName"));
        contactPerson.setLastName(searchParams.get("lastName"));
        contactPerson.setPhoneNumber(searchParams.get("phoneNumber"));
        contactPerson.setEmail(searchParams.get("email"));
        Reservation probe = new Reservation();
        probe.setActivity(activity);
        probe.setContactPerson(contactPerson);
        if (searchParams.containsKey("date")) {
            probe.setDate(LocalDate.parse(searchParams.get("date")));
        }

        // Collecting paths
        List<String> pathList = new ArrayList<>();
        pathList.addAll(getPaths(Reservation.class.getDeclaredFields(), ""));
        pathList.addAll(getPaths(Activity.class.getDeclaredFields(), "activity."));
        pathList.addAll(getPaths(ContactPerson.class.getDeclaredFields(), "contactPerson."));
        pathList.addAll(getPaths(TimeSlot.class.getDeclaredFields(), "timeSlot."));
        String[] pathArray = pathList.toArray(String[]::new);

        // Making the matcher
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnorePaths(pathArray);

        return reservationRepository.findAll(Example.of(probe, matcher));
    }

    public Optional<Reservation> getOne(int id) {
        return reservationRepository.findById(id);
    }

    public boolean delete(int id) {
        if (reservationRepository.findById(id).isEmpty()) return false;
        reservationRepository.deleteById(id);
        return true;
    }

    public boolean updateFromDTO(ReservationDTO reservationDTO) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationDTO.reservationId());
        if (reservation.isEmpty()) return false;
        reservationDTO.update(reservation.get());
        reservationDTO.update(reservation.get().getContactPerson());
        reservationRepository.save(reservation.get());
        return true;
    }

    public int createFromDTO(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservationDTO.update(reservation);
        ContactPerson contactPerson = new ContactPerson();
        reservationDTO.update(contactPerson);
        reservation.setContactPerson(contactPerson);
        contactPersonRepository.save(contactPerson);
        Optional<TimeSlot> timeSlot = timeSlotRepository.findById(reservationDTO.timeSlotId());
        if (timeSlot.isEmpty()) return 0;
        if (timeSlot.get().getActivity().getId() != reservationDTO.activityId()) return 0;
        Optional<Activity> activity = activityRepository.findById(reservationDTO.activityId());
        if (activity.isEmpty()) return 0;
        reservation.setActivity(activity.get());
        reservation.setTimeSlot(timeSlot.get());
        return reservationRepository.save(reservation).getId();
    }

    //

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Map<String, String> getAvailability(String activity, LocalDate date, int numberOfPeople) {
        List<Reservation> reservations = reservationRepository.findAvailability(activity, date);

        List<LocalTime> availableTime = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0));

        Map<String, String> availabilityMap = new HashMap<>();
        int maxNumberOfPeople = 20;

        for (LocalTime availableSlot : availableTime) {
            int bookedSpots = reservations.stream()//en stream af reservationer
                    .filter(r -> convertTimeToSlot(availableSlot) == r.getTimeSlot().getId())//sammenligner tidskode med timeslot fra r (reservationen)
                    .mapToInt(Reservation::getNumberOfPeople) //henter numberofpeople for hver reservation
                    .sum(); //summerer mængden af numberofpeople

            if (bookedSpots == 0) {
                availabilityMap.put(availableSlot.toString(), "available");
            } else if (bookedSpots > 0 && bookedSpots < maxNumberOfPeople) {
                availabilityMap.put(availableSlot.toString(), "limited availability");
            } else {
                availabilityMap.put(availableSlot.toString(), "unavailable");
            }
        }
        return availabilityMap;
    }

    private int convertTimeToSlot(LocalTime time) {
        // Returner kun timerne i tidskoden
        return time.getHour() * 100 + time.getMinute();  // Dvs. 08:00 bliver 800, 08:30 bliver 830
    }

}
