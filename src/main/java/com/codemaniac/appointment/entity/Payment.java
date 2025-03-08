package com.codemaniac.appointment.entity;

import com.codemaniac.appointment.audit.Audit;
import com.codemaniac.appointment.audit.AuditInterceptor;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditInterceptor.class)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "appointment_id", nullable = false)
  private Appointment appointment;

  private String transactionId;
  private double amount;
  private String status;
  private LocalDateTime paymentDate;

  @Embedded private Audit audit = new Audit();
}
