package com.codemaniac.appointment.service;

import com.codemaniac.appointment.audit.Audit;
import com.codemaniac.appointment.entity.AppointmentSlot;
import com.codemaniac.appointment.entity.AppointmentType;
import com.codemaniac.appointment.enums.SlotStatus;
import com.codemaniac.appointment.repository.AppointmentSlotRepository;
import com.codemaniac.appointment.repository.AppointmentTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentSlotService {

  private final AppointmentSlotRepository slotRepository;
  private final AppointmentTypeRepository appointmentTypeRepository;

  /**
   * Generates appointment slots for a given period. - Uses provided start & end times; defaults to
   * `AppointmentType` values if not provided. - Skips weekends if `isWeekendAllowed = false`. -
   * Creates slots at the correct interval based on `durationMinutes`.
   */
  @Transactional
  public void generateSlots(
      final Long appointmentTypeId,
      final LocalDate startDate,
      final LocalDate endDate,
      final LocalTime userStartTime,
      final LocalTime userEndTime) {
    // Fetch appointment type details
    final AppointmentType appointmentType =
        appointmentTypeRepository
            .findById(appointmentTypeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid appointment type ID"));

    // Ensure slots do not already exist within the selected period
    if (slotRepository.existsByDateBetween(startDate, endDate)) {
      throw new IllegalStateException("Slots already exist for this period.");
    }

    // Ensure there are no booked slots within the period
    if (slotRepository.existsByDateBetweenAndStatus(startDate, endDate, SlotStatus.BOOKED)) {
      throw new IllegalStateException("Cannot regenerate slots, as some are already booked.");
    }

    // Determine the final start and end times (use user input if provided, else defaults)
    final LocalTime finalStartTime =
        (userStartTime != null) ? userStartTime : appointmentType.getDefaultStartTime();
    final LocalTime finalEndTime =
        (userEndTime != null) ? userEndTime : appointmentType.getDefaultEndTime();

    final int slotDuration = appointmentType.getDurationMinutes();

    final List<AppointmentSlot> newSlots = new ArrayList<>();

    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      // **Skip weekends if the appointment type does not allow them**
      if (!appointmentType.isWeekendAllowed()
          && (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY
              || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
        log.info("Skipping weekend: {}", currentDate);
        currentDate = currentDate.plusDays(1);
        continue;
      }

      LocalTime slotTime = finalStartTime;
      while (slotTime.plusMinutes(slotDuration).isBefore(finalEndTime)
          || slotTime.plusMinutes(slotDuration).equals(finalEndTime)) {
        final AppointmentSlot slot = new AppointmentSlot();
        slot.setDate(currentDate);
        slot.setStartTime(slotTime);
        slot.setEndTime(slotTime.plusMinutes(slotDuration));
        slot.setAppointmentType(appointmentType);
        slot.setAudit(new Audit()); // Track creation
        newSlots.add(slot);

        slotTime = slotTime.plusMinutes(slotDuration);
      }
      currentDate = currentDate.plusDays(1);
    }

    slotRepository.saveAll(newSlots);

    log.info(
        "Generated {} slots for appointment type '{}' from {} to {}",
        newSlots.size(),
        appointmentType.getName(),
        startDate,
        endDate);
  }

  public List<AppointmentSlot> getAvailableSlotsByDateAndType(final LocalDate date, final Long appointmentTypeId) {
    return slotRepository.findByDateAndStatusAndAppointmentTypeId(date, SlotStatus.AVAILABLE, appointmentTypeId);
  }

  public List<LocalDate> getAvailableDaysInMonth(final int year, final int month, final Long appointmentTypeId) {
    return slotRepository.findAvailableDaysInMonthAndType(month, year, appointmentTypeId);
  }
}
