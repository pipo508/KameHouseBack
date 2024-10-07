package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.RoutineDay;
import com.mygym.kamehouse.services.RoutineDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routinedays")
public class RoutineDayController {

    @Autowired
    private RoutineDayService routineDayService;

    // Obtiene todos los días de rutina
    @GetMapping
    public ResponseEntity<List<RoutineDay>> getAllRoutineDays() {
        List<RoutineDay> routineDays = routineDayService.getAllRoutineDays();
        return ResponseEntity.ok(routineDays);
    }

    // Obtiene un día de rutina específico por su ID
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDay> getRoutineDayById(@PathVariable Long id) {
        return routineDayService.getRoutineDayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crea un nuevo día de rutina
    @PostMapping
    public ResponseEntity<RoutineDay> createRoutineDay(@RequestBody RoutineDay routineDay) {
        RoutineDay createdRoutineDay = routineDayService.createRoutineDay(routineDay);
        return ResponseEntity.ok(createdRoutineDay);
    }

    // Actualiza un día de rutina existente
    @PutMapping("/{id}")
    public ResponseEntity<RoutineDay> updateRoutineDay(@PathVariable Long id, @RequestBody RoutineDay routineDay) {
        RoutineDay updatedRoutineDay = routineDayService.updateRoutineDay(id, routineDay);
        return ResponseEntity.ok(updatedRoutineDay);
    }

    // Elimina un día de rutina
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutineDay(@PathVariable Long id) {
        routineDayService.deleteRoutineDay(id);
        return ResponseEntity.ok().build();
    }

    // Agrega un ejercicio a un día de rutina específico
    @PostMapping("/{routineDayId}/exercises/{exerciseId}")
    public ResponseEntity<?> addExerciseToRoutineDay(
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseId,
            @RequestBody Map<String, Object> exerciseDetails) {
        try {
            int series = (int) exerciseDetails.get("series");
            int repetitions = (int) exerciseDetails.get("repetitions");
            String weight = (String) exerciseDetails.get("weight");
            String waitTime = (String) exerciseDetails.get("waitTime");

            routineDayService.addExerciseToRoutineDay(routineDayId, exerciseId, series, repetitions, weight, waitTime);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Exercise added successfully to routine day");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Elimina un ejercicio específico de un día de rutina
    @DeleteMapping("/{routineDayId}/exercises/{exerciseRoutineDayId}")
    public ResponseEntity<?> removeExerciseFromRoutineDay(
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseRoutineDayId) {
        routineDayService.removeExerciseFromRoutineDay(routineDayId, exerciseRoutineDayId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Exercise removed successfully from routine day");
        return ResponseEntity.ok(response);
    }

    // Elimina todos los ejercicios de un día de rutina específico
    @DeleteMapping("/{routineDayId}/exercises")
    public ResponseEntity<?> removeAllExercisesFromRoutineDay(@PathVariable Long routineDayId) {
        routineDayService.removeAllExercisesFromRoutineDay(routineDayId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "All exercises removed successfully from routine day");
        return ResponseEntity.ok(response);
    }

    // Elimina todos los ejercicios de todos los días de rutina
    @DeleteMapping("/exercises")
    public ResponseEntity<?> removeAllExercisesFromAllRoutineDays() {
        routineDayService.removeAllExercisesFromAllRoutineDays();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All exercises removed successfully from all routine days");
        return ResponseEntity.ok(response);
    }
}