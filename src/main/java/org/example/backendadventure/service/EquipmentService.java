package org.example.backendadventure.service;
import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Equipment;
import org.example.backendadventure.repository.ActivityRepository;
import org.example.backendadventure.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ActivityRepository activityRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, ActivityRepository activityRepository) {
        this.equipmentRepository = equipmentRepository;
        this.activityRepository = activityRepository;
    }

    // Metode til at oprette udstyr for en aktivitet
    public Equipment createEquipmentForActivity(Activity activity, int quantity, int peoplePerUnit) {
        Equipment equipment = new Equipment();
        equipment.setActivity(activity);
        equipment.setQuantity(quantity);
        equipment.setPeoplePerUnit(peoplePerUnit);
        equipment.setMaxPeople(quantity * peoplePerUnit);

        return equipmentRepository.save(equipment);
    }


    public List<Equipment> findByActivityName(String activityName) {
        Activity activity = activityRepository.findByName(activityName);
        if (activity != null) {
            return equipmentRepository.findByActivity(activity);
        }
        return List.of();
    }

    public boolean updateEquipment(String activityName, List<Equipment> updatedEquipmentList) {
        Activity activity = activityRepository.findByName(activityName);
        if (activity != null) {
            List<Equipment> existingEquipmentList = equipmentRepository.findByActivity(activity);

            for (Equipment updatedEquipment : updatedEquipmentList) {
                for (Equipment existingEquipment : existingEquipmentList) {
                    if (existingEquipment.getId() == updatedEquipment.getId()) {
                        existingEquipment.setQuantity(updatedEquipment.getQuantity());
                        existingEquipment.setPeoplePerUnit(updatedEquipment.getPeoplePerUnit());
                        existingEquipment.setMaxPeople(existingEquipment.getQuantity() * existingEquipment.getPeoplePerUnit());
                    }
                }
            }
            equipmentRepository.saveAll(existingEquipmentList);
            return true;
        }
        return false;
    }


    // Metode til at hente alt udstyr for alle aktiviteter som et Map<ActivityName, List<Equipment>>
    public Map<String, List<Equipment>> getAllEquipmentByActivity() {
        List<Activity> activities = activityRepository.findAll();
        Map<String, List<Equipment>> equipmentMap = new HashMap<>();

        for (Activity activity : activities) {
            List<Equipment> equipmentList = equipmentRepository.findByActivity(activity);
            equipmentMap.put(activity.getName(), equipmentList);
        }
        return equipmentMap;
    }

}
