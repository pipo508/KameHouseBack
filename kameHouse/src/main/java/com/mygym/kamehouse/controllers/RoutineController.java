package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.Routine;
import com.mygym.kamehouse.services.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @GetMapping
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List<Routine> routines = routineService.getAllRoutines();
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Routine> getRoutineById(@PathVariable Long id) {
        return routineService.getRoutineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Routine> createRoutine(@RequestBody Routine routine) {
        Routine createdRoutine = routineService.createRoutine(routine);
        return ResponseEntity.ok(createdRoutine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable Long id, @RequestBody Routine routineDetails) {
        Routine updatedRoutine = routineService.updateRoutine(id, routineDetails);
        return ResponseEntity.ok(updatedRoutine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{routineId}/days")
    public ResponseEntity<Routine> addDayToRoutine(@PathVariable Long routineId, @RequestBody String dayName) {
        Routine updatedRoutine = routineService.addDayToRoutine(routineId, dayName);
        return ResponseEntity.ok(updatedRoutine);
    }

    @DeleteMapping("/{routineId}/days/{dayId}")
    public ResponseEntity<Routine> removeDayFromRoutine(@PathVariable Long routineId, @PathVariable Long dayId) {
        Routine updatedRoutine = routineService.removeDayFromRoutine(routineId, dayId);
        return ResponseEntity.ok(updatedRoutine);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Routine> getRoutineByUserId(@PathVariable Long userId) {
        return routineService.getRoutineByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}