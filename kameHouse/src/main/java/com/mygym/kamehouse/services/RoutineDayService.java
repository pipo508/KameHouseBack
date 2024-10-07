package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Exercise;
import com.mygym.kamehouse.models.ExerciseRoutineDay;
import com.mygym.kamehouse.models.RoutineDay;
import com.mygym.kamehouse.repositories.ExerciseRepository;
import com.mygym.kamehouse.repositories.RoutineDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoutineDayService {

    @Autowired
    private RoutineDayRepository routineDayRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public List<RoutineDay> getAllRoutineDays() {
        return routineDayRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RoutineDay> getRoutineDayById(Long id) {
        return routineDayRepository.findById(id);
    }

    @Transactional
    public RoutineDay createRoutineDay(RoutineDay routineDay) {
        return routineDayRepository.save(routineDay);
    }

    @Transactional
    public RoutineDay updateRoutineDay(Long id, RoutineDay routineDayDetails) {
        RoutineDay routineDay = routineDayRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RoutineDay not found with id: " + id));
        routineDay.setDay(routineDayDetails.getDay());
        return routineDayRepository.save(routineDay);
    }

    @Transactional
    public void deleteRoutineDay(Long id) {
        routineDayRepository.deleteById(id);
    }

    @Transactional
    public void addExerciseToRoutineDay(Long routineDayId, Long exerciseId, int series, int repetitions, String weight, String waitTime) {
        RoutineDay routineDay = routineDayRepository.findById(routineDayId)
                .orElseThrow(() -> new IllegalArgumentException("RoutineDay not found with id: " + routineDayId));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + exerciseId));

        ExerciseRoutineDay exerciseRoutineDay = new ExerciseRoutineDay();
        exerciseRoutineDay.setExercise(exercise);
        exerciseRoutineDay.setRoutineDay(routineDay);
        exerciseRoutineDay.setSeries(series);
        exerciseRoutineDay.setRepetitions(repetitions);
        exerciseRoutineDay.setWeight(weight);
        exerciseRoutineDay.setWaitTime(waitTime);

        routineDay.addExerciseRoutineDay(exerciseRoutineDay);
        routineDayRepository.save(routineDay);
    }
}