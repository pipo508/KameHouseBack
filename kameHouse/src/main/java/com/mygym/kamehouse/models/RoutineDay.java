package com.mygym.kamehouse.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "routine_days")
public class RoutineDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @JsonManagedReference
    @OneToMany(mappedBy = "routineDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseRoutineDay> exerciseRoutineDays = new ArrayList<>();

    public void addExerciseRoutineDay(ExerciseRoutineDay exerciseRoutineDay) {
        exerciseRoutineDays.add(exerciseRoutineDay);
        exerciseRoutineDay.setRoutineDay(this);
    }
}