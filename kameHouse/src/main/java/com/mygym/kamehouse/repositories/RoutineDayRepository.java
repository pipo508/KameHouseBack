package com.mygym.kamehouse.repositories;

import com.mygym.kamehouse.models.RoutineDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface RoutineDayRepository extends JpaRepository<RoutineDay, Long> {

}