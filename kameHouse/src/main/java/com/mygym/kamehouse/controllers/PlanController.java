package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.Plan;
import com.mygym.kamehouse.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping
    public void addPlan(@RequestBody Plan plan) {
        planService.addPlan(plan);
    }

    @GetMapping
    public List<Plan> getPlans() {
        return planService.getPlans();
    }

    @GetMapping("/{id}")
    public Optional<Plan> getPlanById(@PathVariable Long id) {
        return planService.getPlanById(id);
    }

    @PutMapping("/{id}")
    public void updatePlan(@PathVariable Long id, @RequestBody Plan plan) {
        plan.setId(id);
        planService.updatePlan(plan);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
    }
}
