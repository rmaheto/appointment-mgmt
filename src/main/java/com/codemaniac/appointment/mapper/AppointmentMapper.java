package com.codemaniac.appointment.mapper;

import com.codemaniac.appointment.dto.AppointmentDto;
import com.codemaniac.appointment.entity.Appointment;

import com.codemaniac.appointment.entity.User;
import com.codemaniac.appointment.enums.AppointmentStatus;
import java.time.LocalDateTime;

public class AppointmentMapper {

    private AppointmentMapper() {
    }

    public static AppointmentDto toDto(final Appointment appointment) {
      if (appointment == null) {
        return null;
      }

      final AppointmentDto dto = new AppointmentDto();
      dto.setId(appointment.getId());

      final User user = appointment.getUser();
      if (user != null) {
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhone());
      }

      final AppointmentStatus status = appointment.getStatus();
      dto.setStatus(status != null ? status.toString() : null);

      final LocalDateTime bookedAt = appointment.getBookedAt();
      dto.setBookedAt(bookedAt);
      dto.setRescheduleCount(appointment.getRescheduleCount());

      dto.setAllowReschedule(
          appointment.getRescheduleCount() <= 5 &&
              bookedAt != null &&
              (bookedAt.isBefore(LocalDateTime.now()) || bookedAt.isEqual(LocalDateTime.now()))
      );

      return dto;
    }


}
