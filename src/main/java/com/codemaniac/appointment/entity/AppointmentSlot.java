package com.codemaniac.appointment.entity;

import com.codemaniac.appointment.audit.Audit;
import com.codemaniac.appointment.audit.AuditInterceptor;
import com.codemaniac.appointment.enums.SlotStatus;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "appointment_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditInterceptor.class)
public class AppointmentSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;

  @Enumerated(EnumType.STRING)
  private SlotStatus status = SlotStatus.AVAILABLE;

  @ManyToOne
  @JoinColumn(name = "appointment_type_id", nullable = false)
  private AppointmentType appointmentType;

  @Embedded
  private Audit audit = new Audit();
}
