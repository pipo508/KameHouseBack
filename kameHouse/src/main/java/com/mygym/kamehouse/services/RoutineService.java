package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Routine;
import com.mygym.kamehouse.repositories.RoutineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

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
        // Update other fields as necessary
        return routineRepository.save(routine);
    }

    @Transactional
    public void deleteRoutine(Long id) {
        routineRepository.deleteById(id);
    }
}