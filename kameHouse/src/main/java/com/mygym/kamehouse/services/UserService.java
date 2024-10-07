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

    @Autowired
    private RoutineDayService routineDayService;

    @Transactional
    public Map<String, Object> addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User createdUser = userRepository.save(user);
        routineService.createRoutineForUser(createdUser);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User created successfully");
        response.put("role", createdUser.getRole());
        return response;
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
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        existingUser.setActive(user.isActive());

        if (!existingUser.getTypePlan().equals(user.getTypePlan())) {
            existingUser.setTypePlan(user.getTypePlan());
            routineService.updateRoutineForUser(existingUser, user.getTypePlan());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        if (user.getRoutine() != null) {
            routineService.deleteRoutine(user.getRoutine().getId());
        }

        userRepository.delete(user);
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

    @Transactional
    public void removeExerciseFromUserRoutineDay(Long userId, Long routineDayId, Long exerciseRoutineDayId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RoutineDay routineDay = user.getRoutine().getRoutineDays().stream()
                .filter(rd -> rd.getId().equals(routineDayId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Routine day not found"));

        routineDay.getExerciseRoutineDays().removeIf(erd -> erd.getId().equals(exerciseRoutineDayId));
        userRepository.save(user);
    }

    @Transactional
    public void removeAllExercisesFromUserRoutineDay(Long userId, Long routineDayId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RoutineDay routineDay = user.getRoutine().getRoutineDays().stream()
                .filter(rd -> rd.getId().equals(routineDayId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Routine day not found"));

        routineDay.getExerciseRoutineDays().clear();
        userRepository.save(user);
    }

    @Transactional
    public void removeAllExercisesFromUserRoutine(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getRoutine().getRoutineDays().forEach(rd -> rd.getExerciseRoutineDays().clear());
        userRepository.save(user);
    }

    @Transactional
    public CustomExercise createCustomExerciseForUser(Long userId, CustomExercise customExercise) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        customExercise.setUser(user);
        user.getCustomExercises().add(customExercise);
        return userRepository.save(user).getCustomExercises().get(user.getCustomExercises().size() - 1);
    }

    @Transactional
    public void deleteCustomExerciseForUser(Long userId, Long customExerciseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getCustomExercises().removeIf(ce -> ce.getId().equals(customExerciseId));
        userRepository.save(user);
    }

    @Transactional
    public void addExerciseToUserRoutineDay(Long userId, Long routineDayId, Long exerciseId, int series, int repetitions, String weight, String waitTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RoutineDay routineDay = user.getRoutine().getRoutineDays().stream()
                .filter(day -> day.getId().equals(routineDayId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Routine day not found for this user"));

        routineDayService.addExerciseToRoutineDay(routineDay.getId(), exerciseId, series, repetitions, weight, waitTime);
    }
}