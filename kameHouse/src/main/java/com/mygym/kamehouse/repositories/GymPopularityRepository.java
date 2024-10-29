package com.mygym.kamehouse.repositories;

import com.mygym.kamehouse.models.GymPopularity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymPopularityRepository extends JpaRepository<GymPopularity, Long> {
    GymPopularity findByDay(String day);
}
