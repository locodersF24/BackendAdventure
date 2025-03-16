package org.example.backendadventure.service;

import jakarta.annotation.PostConstruct;
import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.ContactPerson;
import org.example.backendadventure.model.Equipment;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.ContactPersonRepository;
import org.example.backendadventure.repository.EquipmentRepository;
import org.example.backendadventure.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DummyDataService {

    private final ReservationRepository reservationRepository;
    private final ActivityRepository activityRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final EquipmentService equipmentService;

    public DummyDataService(ReservationRepository reservationRepository,
                            ActivityRepository activityRepository,
                            ContactPersonRepository contactPersonRepository,
                            EquipmentService equipmentService) {
        this.reservationRepository = reservationRepository;
        this.activityRepository = activityRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.equipmentService = equipmentService;
    }

    @PostConstruct
    public void init() {

        int numberOfGeneratedReservations = 50;

        // Make Activities
        List<Activity> activities = new ArrayList<>();
        activities.add(activityRepository.save(new Activity("Go-kart", 10, 10)));
        activities.add(activityRepository.save(new Activity("Minigolf", 10, 10)));
        activities.add(activityRepository.save(new Activity("Paintball", 10, 10)));
        activities.add(activityRepository.save(new Activity("Sumo Wrestling", 10, 10)));

        // Opret og tilknyt udstyr til aktiviteter
        createEquipmentForActivities(activities);


        // Opret udstyr ved hjælp af EquipmentService
        equipmentService.createEquipmentForActivity(activities.get(0), 10, 1);
        equipmentService.createEquipmentForActivity(activities.get(1), 10, 2);
        equipmentService.createEquipmentForActivity(activities.get(2), 10, 2);
        equipmentService.createEquipmentForActivity(activities.get(3), 10, 2);

        // Make random strings
        List<String> randomNames = new ArrayList<>();
        List<String> randomNumbers = new ArrayList<>();
        Random randomGenerator = new Random();
        List<Character> letters = List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
        for (int i = 0; i < numberOfGeneratedReservations * 4; i++) {
            String randomName = "";
            String randomNumber = "";
            for (int j = 0; j < 8; j++) {
                randomName += letters.get(randomGenerator.nextInt(letters.size()));
                randomNumber += randomGenerator.nextInt(10);
            }
            randomNames.add(randomName);
            randomNumbers.add(randomNumber);
        }

        // Make reservations
        for (int i = 0; i < numberOfGeneratedReservations; i++) {
            Reservation reservation = new Reservation(
                    randomGenerator.nextInt(9) + 1,
                    LocalDate.now(),
                    randomGenerator.nextInt(9) + 1,
                    activities.get(randomGenerator.nextInt(activities.size())),
                    contactPersonRepository.save(new ContactPerson(
                            randomNames.removeLast(),
                            randomNames.removeLast(),
                            randomNumbers.removeLast(),
                            randomNames.removeLast() + "@" + randomNames.removeLast() + ".dk"
                    ))
            );
            reservationRepository.save(reservation);
        }

    }

    private void createEquipmentForActivities(List<Activity> activities) {
        // Udstyr for aktiviteter
        Map<String, List<Equipment>> equipmentData = Map.of(


                "Go-kart", List.of(
                        new Equipment(activities.get(0), "Go-kart Car",10, 1, 1)
                ),
                "Minigolf", List.of(
                        new Equipment(activities.get(1), "Golfkølle",10, 1, 2),
                        new Equipment(activities.get(1), "Golfballs",20, 2, 2)
                ),
                "Paintball", List.of(
                        new Equipment(activities.get(2), "Gun", 10, 1, 2)
                ),
                "Sumo Wrestling", List.of(
                        new Equipment(activities.get(3), "Sumo suit",10, 1, 2)
                )
        );


        for (Activity activity : activities) {
            List<Equipment> equipmentList = equipmentData.getOrDefault(activity.getName(), List.of());
            for (Equipment equipment : equipmentList) {
                equipment.setActivity(activity);
                equipmentService.createEquipmentForActivity(activity, equipment.getQuantity(), equipment.getPeoplePerUnit());
            }
        }
    }
}
