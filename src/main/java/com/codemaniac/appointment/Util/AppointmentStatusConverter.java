package com.codemaniac.appointment.Util;

import com.codemaniac.appointment.enums.AppointmentStatus;
import com.codemaniac.appointment.enums.Booked;
import com.codemaniac.appointment.enums.Cancelled;
import com.codemaniac.appointment.enums.Rescheduled;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class AppointmentStatusConverter implements AttributeConverter<AppointmentStatus, String> {

  @Override
  public String convertToDatabaseColumn(final AppointmentStatus status) {
    if (status instanceof Booked) {
      return "BOOKED";
    } else if (status instanceof final Rescheduled rescheduled) {
      return "RESCHEDULED:" + rescheduled.rescheduledAt();
    } else if (status instanceof final Cancelled cancelled) {
      return "CANCELLED:" + cancelled.reason();
    }
    throw new IllegalArgumentException("Unknown status type: " + status);
  }

  @Override
  public AppointmentStatus convertToEntityAttribute(final String dbData) {
    if (dbData.startsWith("RESCHEDULED:")) {
      return new Rescheduled(LocalDateTime.parse(dbData.substring(12)));
    } else if (dbData.startsWith("CANCELLED:")) {
      return new Cancelled(dbData.substring(9));
    } else if (dbData.equals("BOOKED")) {
      return new Booked();
    }
    throw new IllegalArgumentException("Unknown database status: " + dbData);
  }
}
