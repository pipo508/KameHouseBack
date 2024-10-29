package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.GymPopularity;
import com.mygym.kamehouse.repositories.GymPopularityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GymPopularityService {

    @Autowired
    private GymPopularityRepository gymPopularityRepository;

    public List<GymPopularity> getAllPopularity() {
        return gymPopularityRepository.findAll();
    }

    public GymPopularity getPopularityByDay(String day) {
        return gymPopularityRepository.findByDay(day);
    }

    public GymPopularity addPopularity(GymPopularity gymPopularity) {
        return gymPopularityRepository.save(gymPopularity);
    }

    public GymPopularity updatePopularity(Long id, GymPopularity gymPopularity) {
        Optional<GymPopularity> existingPopularity = gymPopularityRepository.findById(id);
        if (existingPopularity.isPresent()) {
            gymPopularity.setId(id);
            return gymPopularityRepository.save(gymPopularity);
        }
        return null; // O lanzar una excepción personalizada
    }

    public void deletePopularity(Long id) {
        Optional<GymPopularity> existingPopularity = gymPopularityRepository.findById(id);
        if (existingPopularity.isPresent()) {
            gymPopularityRepository.deleteById(id);
        } else {
            throw new RuntimeException("GymPopularity not found for id: " + id);
        }
    }
    public Map<String, Object> getPopularityChartData() {
        List<GymPopularity> popularities = gymPopularityRepository.findAll();

        // Crear arrays de etiquetas (días) y datos (popularidades)
        String[] labels = popularities.stream().map(GymPopularity::getDay).toArray(String[]::new);
        int[] data = popularities.stream().mapToInt(GymPopularity::getPopularity).toArray();

        // Construir la respuesta en el formato necesario
        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("datasets", new Object[] { Map.of("data", data) });

        return response;
    }
}
