package com.codemaniac.appointment.controller;

import com.codemaniac.appointment.entity.AppointmentSlot;
import com.codemaniac.appointment.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class AppointmentSlotController {

  private final AppointmentSlotService slotService;

  /**
   * Generates appointment slots for a given period. - If the user provides start & end time, they
   * will be used. - If not, the defaults from `AppointmentType` will be applied.
   */
  @PostMapping("/generate")
  public ResponseEntity<String> generateSlots(
      @RequestParam final Long appointmentTypeId,
      @RequestParam final String startDate,
      @RequestParam final String endDate,
      @RequestParam(required = false) final String startTime,
      @RequestParam(required = false) final String endTime) {
    try {
      final LocalTime parsedStartTime = (startTime != null) ? LocalTime.parse(startTime) : null;
      final LocalTime parsedEndTime = (endTime != null) ? LocalTime.parse(endTime) : null;

      slotService.generateSlots(
          appointmentTypeId,
          LocalDate.parse(startDate),
          LocalDate.parse(endDate),
          parsedStartTime,
          parsedEndTime);
      return ResponseEntity.ok("Appointment slots generated successfully!");
    } catch (final IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (final IllegalArgumentException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/available")
  public ResponseEntity<List<AppointmentSlot>> getAvailableSlots(
      @RequestParam final String date, @RequestParam final Long appointmentTypeId) {
    final List<AppointmentSlot> slots =
        slotService.getAvailableSlotsByDateAndType(LocalDate.parse(date), appointmentTypeId);
    if (slots.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(slots);
  }

  @GetMapping("/available-days")
  public ResponseEntity<List<LocalDate>> getAvailableDays(
      @RequestParam final int year,
      @RequestParam final int month,
      @RequestParam final Long appointmentTypeId) {
    final List<LocalDate> availableDays =
        slotService.getAvailableDaysInMonth(year, month, appointmentTypeId);
    if (availableDays.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(availableDays);
  }
}
