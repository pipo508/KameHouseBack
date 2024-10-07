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

    @GetMapping
    public ResponseEntity<List<RoutineDay>> getAllRoutineDays() {
        List<RoutineDay> routineDays = routineDayService.getAllRoutineDays();
        return ResponseEntity.ok(routineDays);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineDay> getRoutineDayById(@PathVariable Long id) {
        return routineDayService.getRoutineDayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoutineDay> createRoutineDay(@RequestBody RoutineDay routineDay) {
        RoutineDay createdRoutineDay = routineDayService.createRoutineDay(routineDay);
        return ResponseEntity.ok(createdRoutineDay);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineDay> updateRoutineDay(@PathVariable Long id, @RequestBody RoutineDay routineDay) {
        RoutineDay updatedRoutineDay = routineDayService.updateRoutineDay(id, routineDay);
        return ResponseEntity.ok(updatedRoutineDay);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutineDay(@PathVariable Long id) {
        routineDayService.deleteRoutineDay(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{routineDayId}/exercises/{exerciseId}")
    public ResponseEntity<?> addExerciseToRoutineDay(
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseId,
            @RequestBody Map<String, Object> exerciseDetails) {

        int series = (int) exerciseDetails.get("series");
        int repetitions = (int) exerciseDetails.get("repetitions");
        String weight = (String) exerciseDetails.get("weight");
        String waitTime = (String) exerciseDetails.get("waitTime");

        routineDayService.addExerciseToRoutineDay(routineDayId, exerciseId, series, repetitions, weight, waitTime);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Exercise added successfully");
        return ResponseEntity.ok(response);
    }
}