package com.codemaniac.appointment.enums;

public sealed interface AppointmentStatus permits Booked, Rescheduled, Cancelled {}
