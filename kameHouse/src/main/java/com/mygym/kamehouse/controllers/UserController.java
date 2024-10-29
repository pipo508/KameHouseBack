package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.dto.UserLoginDto;
import com.mygym.kamehouse.models.User;
import com.mygym.kamehouse.models.CustomExercise;
import com.mygym.kamehouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            Map<String, Object> response = userService.addUser(user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(authentication.getCredentials()  );
            User user = userService.getUserByEmail(loginDto.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/routine")
    public ResponseEntity<Map<String, Object>> getUserRoutineWithExercises(@PathVariable Long userId) {
        Map<String, Object> routineData = userService.getUserRoutineWithExercises(userId);
        return ResponseEntity.ok(routineData);
    }

    @DeleteMapping("/{userId}/routinedays/{routineDayId}/exercises/{exerciseId}")
    public ResponseEntity<?> removeExerciseFromUserRoutineDay(
            @PathVariable Long userId,
            @PathVariable Long routineDayId,
            @PathVariable Long exerciseId) {
        userService.removeExerciseFromUserRoutineDay(userId, routineDayId, exerciseId);
        return ResponseEntity.ok().body("Ejercicio eliminado del día de rutina del usuario");
    }

    @DeleteMapping("/{userId}/routinedays/{routineDayId}/exercises")
    public ResponseEntity<?> removeAllExercisesFromUserRoutineDay(
            @PathVariable Long userId,
            @PathVariable Long routineDayId) {
        userService.removeAllExercisesFromUserRoutineDay(userId, routineDayId);
        return ResponseEntity.ok().body("All exercises removed from user's routine day");
    }

    @DeleteMapping("/{userId}/routine/exercises")
    public ResponseEntity<?> removeAllExercisesFromUserRoutine(@PathVariable Long userId) {
        userService.removeAllExercisesFromUserRoutine(userId);
        return ResponseEntity.ok().body("All exercises removed from user's routine");
    }

    @PostMapping("/{userId}/custom-exercises")
    public ResponseEntity<CustomExercise> createCustomExerciseForUser(
            @PathVariable Long userId,
            @RequestBody CustomExercise customExercise) {
        CustomExercise createdExercise = userService.createCustomExerciseForUser(userId, customExercise);
        return ResponseEntity.ok(createdExercise);
    }

    @DeleteMapping("/{userId}/custom-exercises/{customExerciseId}")
    public ResponseEntity<?> deleteCustomExerciseForUser(
            @PathVariable Long userId,
            @PathVariable Long customExerciseId) {
        userService.deleteCustomExerciseForUser(userId, customExerciseId);
        return ResponseEntity.ok().body("Custom exercise deleted");
    }

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

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok().body("All users have been deleted");
    }
}