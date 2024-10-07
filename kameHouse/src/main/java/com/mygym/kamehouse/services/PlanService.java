package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Plan;
import com.mygym.kamehouse.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan addPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public List<Plan> getPlans() {
        return planRepository.findAll();
    }

    public Optional<Plan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    public Plan updatePlan(Plan plan) {
        if (planRepository.existsById(plan.getId())) {
            return planRepository.save(plan);
        }
        return null;
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
}