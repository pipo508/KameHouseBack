package com.mygym.kamehouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Setter
@Getter
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String muscleGroup;

    @JsonIgnore
    @OneToMany(mappedBy = "exercise")
    private List<ExerciseRoutineDay> exerciseRoutineDays = new ArrayList<>();
}