package org.example.backendadventure.service;

import org.example.backendadventure.model.*;
import org.example.backendadventure.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSearchRepository searchRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ContactPersonRepository contactPersonRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationSearchRepository searchRepository,
                              AvailabilityRepository availabilityRepository,
                              ContactPersonRepository contactPersonRepository) {
        this.reservationRepository = reservationRepository;
        this.searchRepository = searchRepository;
        this.availabilityRepository = availabilityRepository;
        this.contactPersonRepository = contactPersonRepository;
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

    public Boolean update(Reservation reservation) {
        Optional<Reservation> optional = reservationRepository.findById(reservation.getId());
        if (optional.isEmpty()) return null;
        Reservation r = reservationRepository.save(reservation);
        if (availabilityRepository.isNumberOfPeopleIsTooHigh(r)) {
            reservationRepository.deleteById(r.getId());
            reservationRepository.save(reservation);
            return false;
        }
        return true;
    }

    public Reservation createReservation(Reservation reservation) {
        ContactPerson contactPerson = reservation.getContactPerson();
        contactPersonRepository.save(contactPerson);
        Reservation r = reservationRepository.save(reservation);
        /*
        if (availabilityRepository.isNumberOfPeopleIsTooHigh(r)) {
            contactPersonRepository.deleteById(r.getContactPerson().getId());
            reservationRepository.deleteById(r.getId());
            return null;
        }*/
        return r;
    }

}
