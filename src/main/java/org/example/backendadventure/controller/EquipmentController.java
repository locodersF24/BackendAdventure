package org.example.backendadventure.controller;
import org.example.backendadventure.model.Equipment;
import org.example.backendadventure.model.Reservation;
import org.example.backendadventure.service.EquipmentService;
import org.example.backendadventure.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
    }

    @PutMapping("/equipment/{activity}")
    public ResponseEntity<List<Equipment>> updateEquipment(
            @PathVariable String activity,
            @RequestBody List<Equipment> updatedEquipmentList) {

        boolean success = equipmentService.updateEquipment(activity, updatedEquipmentList);
        if (success) {
            List<Equipment> updatedEquipment = equipmentService.findByActivityName(activity);
            return ResponseEntity.ok(updatedEquipment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/equipment/{activity}")
    public ResponseEntity<List<Equipment>> getEquipmentByActivity(@PathVariable String activity) {
        List<Equipment> equipmentList = equipmentService.findByActivityName(activity);
        if (equipmentList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Equipment>>> getAllEquipmentByActivity() {
    Map<String,List<Equipment>> equipmentMap = equipmentService.getAllEquipmentByActivity();
    return ResponseEntity.ok(equipmentMap);
    }
}