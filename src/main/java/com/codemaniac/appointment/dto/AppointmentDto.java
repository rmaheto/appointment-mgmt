package com.codemaniac.appointment.dto;

import com.codemaniac.appointment.entity.AppointmentSlot;
import com.codemaniac.appointment.entity.AppointmentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AppointmentSlot appointmentSlot;
    private AppointmentType appointmentType;
    private String status;
    private LocalDateTime bookedAt;
    private int rescheduleCount;
    private boolean allowReschedule;

}
