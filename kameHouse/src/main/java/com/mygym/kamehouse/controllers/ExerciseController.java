package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.Exercise;
import com.mygym.kamehouse.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    // Método para agregar un solo ejercicio
    @PostMapping("/single")
    public ResponseEntity<Exercise> addExercise(@RequestBody Exercise exercise) {
        Exercise createdExercise = exerciseService.saveExercise(exercise);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    // Método para agregar múltiples ejercicios
    @PostMapping("/multiple")
    public ResponseEntity<List<Exercise>> addExercises(@RequestBody List<Exercise> exercises) {
        List<Exercise> createdExercises = exerciseService.saveExercises(exercises);
        return new ResponseEntity<>(createdExercises, HttpStatus.CREATED);
    }

    // Método para obtener todos los ejercicios
    @GetMapping
    public List<Exercise> getExercises() {
        return exerciseService.getAllExercises();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Método para obtener ejercicios por grupo muscular
    @GetMapping("/muscleGroup/{muscleGroup}")
    public List<Exercise> getExercisesByMuscleGroup(@PathVariable String muscleGroup) {
        return exerciseService.findExercisesByMuscleGroup(muscleGroup);
    }

    // Método para actualizar un ejercicio
    @PutMapping("/{id}")
    public void updateExercise(@PathVariable Long id, @RequestBody Exercise exerciseDetails) {
        exerciseService.updateExercise(id, exerciseDetails);
    }

    // Método para eliminar un ejercicio
    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
    }
}
