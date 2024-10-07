package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.CustomExercise;
import com.mygym.kamehouse.models.User;
import com.mygym.kamehouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Crea un nuevo usuario
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> response = userService.addUser(user);
        return ResponseEntity.ok(response);
    }

    // Obtiene todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    // Obtiene un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualiza un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Elimina un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // Inicia sesión de usuario
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        return userService.getUserByEmailAndPassword(email, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtiene la rutina de un usuario con sus ejercicios
    @GetMapping("/{userId}/routine")
    public ResponseEntity<Map<String, Object>> getUserRoutineWithExercises(@PathVariable Long userId) {
        Map<String, Object> routineData = userService.getUserRoutineWithExercises(userId);
        return ResponseEntity.ok(routineData);
    }

    // Elimina un ejercicio específico de un día de rutina de un usuario
    @DeleteMapping("/{userId}/routinedays/{routineDayId}/exercises/{exerciseRoutineDayId}")
    public ResponseEntity<?> removeExerciseFromUserRoutineDay(
            @PathVariable Long userId,
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseRoutineDayId) {
        userService.removeExerciseFromUserRoutineDay(userId, routineDayId, exerciseRoutineDayId);
        return ResponseEntity.ok().body("Exercise removed from user's routine day");
    }

    // Elimina todos los ejercicios de un día de rutina específico de un usuario
    @DeleteMapping("/{userId}/routinedays/{routineDayId}/exercises")
    public ResponseEntity<?> removeAllExercisesFromUserRoutineDay(
            @PathVariable Long userId,
            @PathVariable Long routineDayId) {
        userService.removeAllExercisesFromUserRoutineDay(userId, routineDayId);
        return ResponseEntity.ok().body("All exercises removed from user's routine day");
    }

    // Elimina todos los ejercicios de toda la rutina de un usuario
    @DeleteMapping("/{userId}/routine/exercises")
    public ResponseEntity<?> removeAllExercisesFromUserRoutine(@PathVariable Long userId) {
        userService.removeAllExercisesFromUserRoutine(userId);
        return ResponseEntity.ok().body("All exercises removed from user's routine");
    }

    // Crea un ejercicio personalizado para un usuario
    @PostMapping("/{userId}/custom-exercises")
    public ResponseEntity<CustomExercise> createCustomExerciseForUser(
            @PathVariable Long userId,
            @RequestBody CustomExercise customExercise) {
        CustomExercise createdExercise = userService.createCustomExerciseForUser(userId, customExercise);
        return ResponseEntity.ok(createdExercise);
    }

    // Elimina un ejercicio personalizado de un usuario
    @DeleteMapping("/{userId}/custom-exercises/{customExerciseId}")
    public ResponseEntity<?> deleteCustomExerciseForUser(
            @PathVariable Long userId,
            @PathVariable Long customExerciseId) {
        userService.deleteCustomExerciseForUser(userId, customExerciseId);
        return ResponseEntity.ok().body("Custom exercise deleted");
    }

    // Agrega un ejercicio a un día de rutina específico de un usuario
    @PostMapping("/{userId}/routinedays/{routineDayId}/exercises/{exerciseId}")
    public ResponseEntity<?> addExerciseToUserRoutineDay(
            @PathVariable Long userId,
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseId,
            @RequestBody Map<String, Object> exerciseDetails) {
        try {
            int series = (int) exerciseDetails.get("series");
            int repetitions = (int) exerciseDetails.get("repetitions");
            String weight = (String) exerciseDetails.get("weight");
            String waitTime = (String) exerciseDetails.get("waitTime");

            userService.addExerciseToUserRoutineDay(userId, routineDayId, exerciseId, series, repetitions, weight, waitTime);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Exercise added successfully to user's routine day");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}