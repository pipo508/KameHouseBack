package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.Exercise;
import com.mygym.kamehouse.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {
        Exercise createdExercise = exerciseService.createExercise(exercise);
        return ResponseEntity.ok(createdExercise);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Exercise>> createExercises(@RequestBody List<Exercise> exercises) {
        List<Exercise> createdExercises = exerciseService.createExercises(exercises);
        return ResponseEntity.ok(createdExercises);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Long id, @RequestBody Exercise exerciseDetails) {
        Exercise updatedExercise = exerciseService.updateExercise(id, exerciseDetails);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok().body("Exercise deleted");
    }
    @GetMapping("/by-muscle-group/{muscleGroup}")
    public ResponseEntity<List<Exercise>> getExercisesByMuscleGroup(@PathVariable String muscleGroup) {
        List<Exercise> exercises = exerciseService.findExercisesByMuscleGroup(muscleGroup);
        return ResponseEntity.ok(exercises);
    }



}