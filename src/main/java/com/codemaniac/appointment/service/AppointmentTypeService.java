package com.codemaniac.appointment.service;

import com.codemaniac.appointment.dto.AppointmentTypeRequest;
import com.codemaniac.appointment.entity.AppointmentType;
import com.codemaniac.appointment.repository.AppointmentTypeRepository;
import com.codemaniac.appointment.mapper.AppointmentTypeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentTypeService {

  private final AppointmentTypeRepository repository;

  @Transactional
  public AppointmentType createAppointmentType(final AppointmentTypeRequest type) {
    // Ensure the name is unique
    if (repository.existsByName(type.name())) {
      throw new IllegalArgumentException(
          "Appointment type with name '" + type.name() + "' already exists.");
    }

    // Ensure valid working hours
    if (type.defaultStartTime().isAfter(type.defaultEndTime())) {
      throw new IllegalArgumentException("Start time must be before end time.");
    }

    final AppointmentType appointmentType = AppointmentTypeMapper.toEntity(type);
    return repository.save(appointmentType);
  }

  public List<AppointmentType> getAllAppointmentTypes() {
    return repository.findAll();
  }

  public AppointmentType getAppointmentTypeById(final Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Appointment type not found with ID: " + id));
  }

  @Transactional
  public AppointmentType updateAppointmentType(final Long id, final AppointmentType updatedType) {
    final AppointmentType existingType =
        repository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Appointment type not found with ID: " + id));

    // Ensure valid working hours
    if (updatedType.getDefaultStartTime().isAfter(updatedType.getDefaultEndTime())) {
      throw new IllegalArgumentException("Start time must be before end time.");
    }

    existingType.setName(updatedType.getName());
    existingType.setDescription(updatedType.getDescription());
    existingType.setDurationMinutes(updatedType.getDurationMinutes());
    existingType.setWeekendAllowed(updatedType.isWeekendAllowed());
    existingType.setMaxReschedules(updatedType.getMaxReschedules());
    existingType.setDefaultStartTime(updatedType.getDefaultStartTime());
    existingType.setDefaultEndTime(updatedType.getDefaultEndTime());

    return repository.save(existingType);
  }

  @Transactional
  public void deleteAppointmentType(final Long id) {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Appointment type not found with ID: " + id);
    }
    repository.deleteById(id);
  }
}
