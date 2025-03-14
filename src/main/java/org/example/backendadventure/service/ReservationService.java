package org.example.backendadventure.service;

import org.example.backendadventure.model.*;
import org.example.backendadventure.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSearchRepository searchRepository;
    private final ActivityRepository activityRepository;
    private final AvailabilityRepository availabilityRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ContactPersonRepository contactPersonRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationSearchRepository searchRepository,
                              ActivityRepository activityRepository,
                              AvailabilityRepository availabilityRepository,
                              ContactPersonRepository contactPersonRepository,
                              TimeSlotRepository timeSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.searchRepository = searchRepository;
        this.activityRepository = activityRepository;
        this.availabilityRepository = availabilityRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<Reservation> searchReservations(Map<String, String> searchParams) {
        Map<String, String> noBlanks = new HashMap<>();
        searchParams.forEach((key, value) -> {
            if (value != null && !value.isBlank()) noBlanks.put(key, value);
        });
        return searchRepository.search(noBlanks);
    }

    public Optional<Reservation> getOne(int id) {
        return reservationRepository.findById(id);
    }

    public boolean delete(int id) {
        if (reservationRepository.findById(id).isEmpty()) return false;
        reservationRepository.deleteById(id);
        return true;
    }

    public Boolean updateFromDTO(ReservationDTO reservationDTO) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationDTO.reservationId());
        if (reservation.isEmpty()) return null;
        reservationDTO.update(reservation.get());
        reservationDTO.update(reservation.get().getContactPerson());
        if (availabilityRepository.isNumberOfPeopleIsTooHigh(reservation.get())) return false;
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
        if (timeSlot.isEmpty()) return -1;
        if (timeSlot.get().getActivity().getId() != reservationDTO.activityId()) return -1;
        Optional<Activity> activity = activityRepository.findById(reservationDTO.activityId());
        if (activity.isEmpty()) return -1;
        reservation.setActivity(activity.get());
        reservation.setTimeSlot(timeSlot.get());
        if (availabilityRepository.isNumberOfPeopleIsTooHigh(reservation)) return -2;
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
