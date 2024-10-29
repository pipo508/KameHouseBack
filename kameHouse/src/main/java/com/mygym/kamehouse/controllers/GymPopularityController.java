package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.GymPopularity;
import com.mygym.kamehouse.services.GymPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gym-popularity")
public class GymPopularityController {

    @Autowired
    private GymPopularityService gymPopularityService;

    @GetMapping
    public ResponseEntity<List<GymPopularity>> getAllPopularity() {
        List<GymPopularity> popularities = gymPopularityService.getAllPopularity();
        return ResponseEntity.ok(popularities);
    }

    @GetMapping("/{day}")
    public ResponseEntity<GymPopularity> getPopularityByDay(@PathVariable String day) {
        GymPopularity popularity = gymPopularityService.getPopularityByDay(day);
        if (popularity != null) {
            return ResponseEntity.ok(popularity);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GymPopularity> addPopularity(@RequestBody GymPopularity gymPopularity) {
        GymPopularity savedPopularity = gymPopularityService.addPopularity(gymPopularity);
        return ResponseEntity.ok(savedPopularity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymPopularity> updatePopularity(@PathVariable Long id, @RequestBody GymPopularity gymPopularity) {
        GymPopularity updatedPopularity = gymPopularityService.updatePopularity(id, gymPopularity);
        if (updatedPopularity != null) {
            return ResponseEntity.ok(updatedPopularity);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePopularity(@PathVariable Long id) {
        try {
            gymPopularityService.deletePopularity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/chart")
    public ResponseEntity<Map<String, Object>> getPopularityChart() {
        Map<String, Object> chartData = gymPopularityService.getPopularityChartData();
        return ResponseEntity.ok(chartData);
    }

}
