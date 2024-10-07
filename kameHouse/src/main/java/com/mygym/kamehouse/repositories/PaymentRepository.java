package com.mygym.kamehouse.repositories;

import com.mygym.kamehouse.models.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    // Puedes añadir métodos personalizados si es necesario
}
