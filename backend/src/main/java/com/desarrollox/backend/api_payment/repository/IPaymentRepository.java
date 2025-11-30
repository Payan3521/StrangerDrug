package com.desarrollox.backend.api_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.desarrollox.backend.api_payment.model.Payment;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    
}
