package com.codemaniac.appointment.entity;

import com.codemaniac.appointment.audit.Audit;
import com.codemaniac.appointment.audit.AuditInterceptor;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditInterceptor.class)
public class AppointmentType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private int durationMinutes;

  private boolean isWeekendAllowed;
  private int maxReschedules = 0;
  private LocalTime defaultStartTime = LocalTime.of(9, 0); // Default to 9 AM
  private LocalTime defaultEndTime = LocalTime.of(17, 0);
  private boolean requiresPayment;
  private double appointmentFee;

  @Embedded
  private Audit audit = new Audit();
}
