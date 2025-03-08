package com.codemaniac.appointment.dto;

public record RescheduleRequest(Long appointmentId, Long newSlotId) {}
