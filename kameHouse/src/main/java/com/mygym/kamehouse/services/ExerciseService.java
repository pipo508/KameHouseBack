package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Exercise;
import com.mygym.kamehouse.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public Exercise updateExercise(Long id, Exercise exerciseDetails) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
        exercise.setName(exerciseDetails.getName());
        exercise.setMuscleGroup(exerciseDetails.getMuscleGroup());
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Exercise> getExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public List<Exercise> createExercises(List<Exercise> exercises) {
        return exerciseRepository.saveAll(exercises);
    }

    @Transactional
    public List<Exercise> findExercisesByMuscleGroup(String muscleGroup) {
        return exerciseRepository.findAll().stream()
                .filter(exercise -> exercise.getMuscleGroup().equalsIgnoreCase(muscleGroup))
                .collect(Collectors.toList());
    }
}
