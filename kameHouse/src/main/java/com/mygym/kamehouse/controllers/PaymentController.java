package com.mygym.kamehouse.controllers;

import com.mygym.kamehouse.models.Payment;
import com.mygym.kamehouse.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/users/{userId}")
    public ResponseEntity<Payment> recordPayment(@PathVariable Long userId, @RequestBody Payment payment) {
        Payment recordedPayment = paymentService.recordPayment(userId, payment);
        return ResponseEntity.ok(recordedPayment);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable Long userId) {
        List<Payment> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/plan-prices")
    public ResponseEntity<Map<String, Double>> getPlanPrices() {
        return ResponseEntity.ok(paymentService.getPlanPrices());
    }

    @PutMapping("/plan-prices")
    public ResponseEntity<?> updatePlanPrices(@RequestBody Map<String, Double> prices) {
        paymentService.updatePlanPrices(prices);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/send-confirmation")
    public ResponseEntity<?> sendPaymentConfirmation(@PathVariable Long userId, @RequestBody Map<String, Object> requestBody) {
        String amountStr = (String) requestBody.get("amount");
        String plan = (String) requestBody.get("plan");

        // Validar si amount y plan son válidos
        if (amountStr == null || plan == null) {
            return ResponseEntity.badRequest().body("Monto y tipo de plan son requeridos");
        }

        try {
            paymentService.sendPaymentConfirmation(userId, Map.of("amount", amountStr), plan);
            return ResponseEntity.ok().body("Confirmación de pago enviada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar la confirmación de pago");
        }
    }

}

