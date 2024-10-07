package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Routine;
import com.mygym.kamehouse.models.RoutineDay;
import com.mygym.kamehouse.models.User;
import com.mygym.kamehouse.repositories.RoutineRepository;
import com.mygym.kamehouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Routine> getRoutineById(Long id) {
        return routineRepository.findById(id);
    }

    @Transactional
    public Routine createRoutine(Routine routine) {
        return routineRepository.save(routine);
    }

    @Transactional
    public Routine updateRoutine(Long id, Routine routineDetails) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + id));

        routine.setName(routineDetails.getName());
        // Aquí puedes actualizar otros campos si es necesario

        return routineRepository.save(routine);
    }

    @Transactional
    public void deleteRoutine(Long id) {
        routineRepository.deleteById(id);
    }

    @Transactional
    public Routine addDayToRoutine(Long routineId, String dayName) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        RoutineDay newDay = new RoutineDay();
        newDay.setDay(dayName);
        newDay.setRoutine(routine);
        routine.getRoutineDays().add(newDay);

        return routineRepository.save(routine);
    }

    @Transactional
    public Routine removeDayFromRoutine(Long routineId, Long dayId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        routine.getRoutineDays().removeIf(day -> day.getId().equals(dayId));
        return routineRepository.save(routine);
    }

    @Transactional(readOnly = true)
    public Optional<Routine> getRoutineByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return Optional.ofNullable(user.getRoutine());
    }

    @Transactional
    public void updateRoutineForUser(User user, String newPlanType) {
        Routine existingRoutine = user.getRoutine();
        if (existingRoutine != null) {
            existingRoutine.getRoutineDays().clear();
            existingRoutine.setName("Rutina de " + user.getName());
        } else {
            existingRoutine = new Routine();
            existingRoutine.setName("Rutina de " + user.getName());
            existingRoutine.setUser(user);
            user.setRoutine(existingRoutine);
        }

        int days = getDaysPerPlan(newPlanType);
        for (int i = 1; i <= days; i++) {
            RoutineDay routineDay = new RoutineDay();
            routineDay.setDay("DÍA " + i);
            existingRoutine.addRoutineDay(routineDay);
        }

        routineRepository.save(existingRoutine);
    }

    @Transactional
    public void createRoutineForUser(User user) {
        Routine routine = new Routine();
        routine.setName("Rutina de " + user.getName());
        routine.setUser(user);

        int days = getDaysPerPlan(user.getTypePlan());
        for (int i = 1; i <= days; i++) {
            RoutineDay routineDay = new RoutineDay();
            routineDay.setDay("DÍA " + i);
            routine.addRoutineDay(routineDay);
        }

        routineRepository.save(routine);
        user.setRoutine(routine);
    }

    private int getDaysPerPlan(String typePlan) {
        switch (typePlan.toUpperCase()) {
            case "PRINCIPIANTE":
                return 2;
            case "INTERMEDIO":
                return 4;
            case "AVANZADO":
                return 6;
            default:
                return 0;
        }
    }
}