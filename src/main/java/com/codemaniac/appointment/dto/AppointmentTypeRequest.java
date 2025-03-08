package com.codemaniac.appointment.dto;

import java.time.LocalTime;

public record AppointmentTypeRequest(
    Long id,
    String name,
    String description,
    int durationMinutes,
    boolean isWeekendAllowed,
    int maxReschedules,
    LocalTime defaultStartTime,
    LocalTime defaultEndTime,
    boolean requiresPayment,
    double appointmentFee) {}
