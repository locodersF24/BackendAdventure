package org.example.backendadventure.repository;

import org.example.backendadventure.model.Activity;
import org.example.backendadventure.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    List<Equipment> findByActivity(Activity activity);

}
