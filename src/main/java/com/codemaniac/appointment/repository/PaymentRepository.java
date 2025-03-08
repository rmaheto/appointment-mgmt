package com.codemaniac.appointment.repository;

import com.codemaniac.appointment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
