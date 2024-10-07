package com.mygym.kamehouse.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exercise_routine_days")
public class ExerciseRoutineDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "routine_day_id")
    private RoutineDay routineDay;

    private int series;
    private int repetitions;
    private String weight;
    private String waitTime;
}