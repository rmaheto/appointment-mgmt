package com.codemaniac.appointment.mapper;

import com.codemaniac.appointment.dto.AppointmentDto;
import com.codemaniac.appointment.entity.Appointment;

import java.time.LocalDateTime;

public class AppointmentMapper {

    private AppointmentMapper() {
    }

    public static AppointmentDto toDto(Appointment appointment) {
        final AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setFirstName(appointment.getUser().getFirstName());
        dto.setLastName(appointment.getUser().getLastName());
        dto.setEmail(appointment.getUser().getEmail());
        dto.setPhoneNumber(appointment.getUser().getPhone());
        dto.setStatus(appointment.getStatus().toString());
        dto.setBookedAt(appointment.getBookedAt());
        dto.setRescheduleCount(appointment.getRescheduleCount());
        dto.setAllowReschedule(appointment.getRescheduleCount() <= 5 &&
                (appointment.getBookedAt().isBefore(LocalDateTime.now()) ||
                        appointment.getBookedAt().isEqual(LocalDateTime.now())));
        return dto;
    }


}
