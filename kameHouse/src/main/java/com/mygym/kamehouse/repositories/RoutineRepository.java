package com.mygym.kamehouse.repositories;

import com.mygym.kamehouse.models.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    // Puedes agregar métodos de consulta personalizados aquí si los necesitas
}