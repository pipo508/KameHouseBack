package com.mygym.kamehouse.services;

import com.mygym.kamehouse.models.Payment;
import com.mygym.kamehouse.models.User;
import com.mygym.kamehouse.repositories.PaymentRepository;
import com.mygym.kamehouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Mapa en memoria para almacenar los precios de los planes
    private final Map<String, Double> planPrices = new HashMap<>();

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          UserRepository userRepository,
                          EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;

        // Inicializar los precios de los planes
        initializePlanPrices();
    }

    private void initializePlanPrices() {
        planPrices.put("PRINCIPIANTE", 1000.0);
        planPrices.put("INTERMEDIO", 1500.0);
        planPrices.put("AVANZADO", 2000.0);
    }

    @Transactional
    public Payment recordPayment(Long userId, Payment payment) {
        logger.info("Recording payment for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        payment.setUser(user);
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment recorded successfully. Payment ID: {}", savedPayment.getId());
        return savedPayment;
    }

    @Transactional(readOnly = true)
    public List<Payment> getUserPayments(Long userId) {
        logger.info("Fetching payments for user ID: {}", userId);
        return paymentRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(Long paymentId) {
        logger.info("Fetching payment with ID: {}", paymentId);
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));
    }

    @Transactional
    public void deletePayment(Long paymentId) {
        logger.info("Deleting payment with ID: {}", paymentId);
        paymentRepository.deleteById(paymentId);
    }

    @Transactional(readOnly = true)
    public Map<String, Double> getPlanPrices() {
        logger.info("Fetching all plan prices");
        return new HashMap<>(planPrices);
    }

    @Transactional
    public void updatePlanPrices(Map<String, Double> prices) {
        logger.info("Updating plan prices: {}", prices);
        planPrices.putAll(prices);
        logger.info("Plan prices updated successfully");
    }

    @Transactional
    public void sendPaymentConfirmation(Long userId, Map<String, String> paymentDetails, String plan) {
        logger.info("Sending payment confirmation for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + userId));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(Double.parseDouble(paymentDetails.get("amount")));
        payment.setPlanPaid(plan); // Establecer el plan recibido
        payment.setPaymentStatus("CONFIRMADO");
        paymentRepository.save(payment);

        String subject = "Confirmación de Pago - MyGym";
        String text = String.format(
                "Estimado %s,\n\nSe ha registrado un pago por el plan %s por un monto de $%s en la fecha %s.\n\nGracias por su preferencia,\nMyGym",
                user.getName(),
                payment.getPlanPaid(),
                payment.getAmount(),
                payment.getPaymentDate()
        );

        try {
            emailService.sendPaymentConfirmation(user.getEmail(), subject, text);
            logger.info("Payment confirmation email sent successfully to user ID: {}", userId);
        } catch (Exception e) {
            logger.error("Error sending payment confirmation email to user ID: {}. Error: {}", userId, e.getMessage());
            // Aquí puedes decidir si quieres lanzar la excepción o manejarla de otra manera
            throw new RuntimeException("Error al enviar el correo de confirmación de pago", e);
        }
    }
}