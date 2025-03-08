package com.codemaniac.appointment.enums;

import java.time.LocalDateTime;

public record Rescheduled(LocalDateTime rescheduledAt) implements AppointmentStatus{

}
