package com.mygym.kamehouse.models;

import com.mygym.kamehouse.models.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments") // Nombre de la tabla
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Nombre de la columna en la tabla Payment que referencia a User
    private User user;

    private String method;
    private String amount;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "expiration_date")
    private String expirationDate;

    private boolean status;
}
