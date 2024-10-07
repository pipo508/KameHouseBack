package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.*;
import com.mygym.kamehouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoutineService routineService;

    @Transactional
    public Map<String, Object> addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User createdUser = userRepository.save(user);

        Routine routine = new Routine();
        routine.setName("Rutina de " + user.getName());
        routine.setUser(createdUser);

        int days = getDaysPerPlan(user.getTypePlan());
        for (int i = 1; i <= days; i++) {
            RoutineDay routineDay = new RoutineDay();
            routineDay.setDay("DÍA " + i);
            routine.addRoutineDay(routineDay);
        }

        routineService.createRoutine(routine);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User created successfully");
        response.put("role", createdUser.getRole());
        return response;
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

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserRoutineWithExercises(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Routine routine = user.getRoutine();
        if (routine == null) {
            throw new IllegalStateException("User does not have a routine");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("routineId", routine.getId());
        result.put("routineName", routine.getName());

        List<Map<String, Object>> routineDays = new ArrayList<>();
        for (RoutineDay day : routine.getRoutineDays()) {
            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("dayId", day.getId());
            dayMap.put("dayName", day.getDay());

            List<Map<String, Object>> exercises = new ArrayList<>();
            for (ExerciseRoutineDay erd : day.getExerciseRoutineDays()) {
                Map<String, Object> exerciseMap = new HashMap<>();
                exerciseMap.put("exerciseId", erd.getExercise().getId());
                exerciseMap.put("exerciseName", erd.getExercise().getName());
                exerciseMap.put("muscleGroup", erd.getExercise().getMuscleGroup());
                exerciseMap.put("series", erd.getSeries());
                exerciseMap.put("repetitions", erd.getRepetitions());
                exerciseMap.put("weight", erd.getWeight());
                exerciseMap.put("waitTime", erd.getWaitTime());
                exercises.add(exerciseMap);
            }
            dayMap.put("exercises", exercises);
            routineDays.add(dayMap);
        }
        result.put("routineDays", routineDays);

        return result;
    }
}