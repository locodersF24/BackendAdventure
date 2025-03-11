package org.example.backendadventure.service;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Booking;
import org.example.backendadventure.model.ContactPerson;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.zip.DataFormatException;

@Service
public class BookingService {

    private final ReservationRepository reservationRepository;

    public BookingService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    private List<String> getPaths(Field[] fields, String prefix) {
        List<String> attributes = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(int.class)) {
                attributes.add(prefix + field.getName());
            }
        }
        return attributes;
    }

    public List<Booking> searchBookings(Map<String, String> searchParams) throws DateTimeParseException {

        // No blank parameters
        Map<String, String> params = new HashMap<>();
        searchParams.forEach((key, value) -> {
            if (!value.isBlank()) {
                params.put(key, value);
            }
        });

        // Making the probe
        Reservation probe = new Reservation();
        if (params.containsKey("date")) {
            probe.setDate(LocalDate.parse(params.get("date")));
        }
        Activity activity = new Activity();
        activity.setName(params.get("activity"));
        probe.setActivity(activity);
        probe.setContactPerson(new ContactPerson(
                params.get("firstName"),
                params.get("lastName"),
                params.get("phoneNumber"),
                params.get("email")
        ));

        // Collecting paths
        List<String> pathList = new ArrayList<>();
        pathList.addAll(getPaths(Reservation.class.getDeclaredFields(), ""));
        pathList.addAll(getPaths(Activity.class.getDeclaredFields(), "activity."));
        pathList.addAll(getPaths(ContactPerson.class.getDeclaredFields(), "contactPerson."));
        String[] pathArray = pathList.toArray(String[]::new);

        // Making the matcher
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnorePaths(pathArray);

        return reservationRepository.findAll(Example.of(probe, matcher))
                .stream()
                .map(Reservation::toBooking)
                .toList();
    }

}
