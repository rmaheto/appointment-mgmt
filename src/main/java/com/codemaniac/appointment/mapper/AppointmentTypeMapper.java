package com.codemaniac.appointment.mapper;

import com.codemaniac.appointment.dto.AppointmentTypeRequest;
import com.codemaniac.appointment.entity.AppointmentType;

public class AppointmentTypeMapper {

    private AppointmentTypeMapper() {
    }

    public static AppointmentType toEntity(final AppointmentTypeRequest type) {
        final AppointmentType appointmentType = new AppointmentType();
        appointmentType.setId(type.id());
        appointmentType.setName(type.name());
        appointmentType.setDescription(type.description());
        appointmentType.setDurationMinutes(type.durationMinutes());
        appointmentType.setWeekendAllowed(type.isWeekendAllowed());
        appointmentType.setRequiresPayment(type.requiresPayment());
        appointmentType.setAppointmentFee(type.appointmentFee());
        if (type.defaultStartTime() != null) {
            appointmentType.setDefaultStartTime(type.defaultStartTime());
        }
        if (type.defaultEndTime() != null) {
            appointmentType.setDefaultEndTime(type.defaultEndTime());
        }
        return appointmentType;
    }
}
