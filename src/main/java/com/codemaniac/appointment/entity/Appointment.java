package com.codemaniac.appointment.entity;

import com.codemaniac.appointment.Util.AppointmentStatusConverter;
import com.codemaniac.appointment.audit.Audit;
import com.codemaniac.appointment.audit.AuditInterceptor;
import com.codemaniac.appointment.enums.AppointmentStatus;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditInterceptor.class)
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "slot_id", nullable = false)
  private AppointmentSlot appointmentSlot;

  @ManyToOne
  @JoinColumn(name = "type_id", nullable = false)
  private AppointmentType appointmentType;

  @Convert(converter = AppointmentStatusConverter.class)
  private AppointmentStatus status;

  private LocalDateTime bookedAt = LocalDateTime.now();

  private int rescheduleCount = 0;

  @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
  private Payment payment;

  @Embedded
  private Audit audit = new Audit();
}
