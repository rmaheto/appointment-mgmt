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
      @RequestParam Long appointmentTypeId,
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam(required = false) String startTime,
      @RequestParam(required = false) String endTime) {
    try {
      LocalTime parsedStartTime = (startTime != null) ? LocalTime.parse(startTime) : null;
      LocalTime parsedEndTime = (endTime != null) ? LocalTime.parse(endTime) : null;

      slotService.generateSlots(
          appointmentTypeId,
          LocalDate.parse(startDate),
          LocalDate.parse(endDate),
          parsedStartTime,
          parsedEndTime);
      return ResponseEntity.ok("Appointment slots generated successfully!");
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/available")
  public ResponseEntity<List<AppointmentSlot>> getAvailableSlots(@RequestParam String date) {
    List<AppointmentSlot> slots = slotService.getAvailableSlotsByDate(LocalDate.parse(date));
    if (slots.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(slots);
  }

  @GetMapping("/available-days")
  public ResponseEntity<List<LocalDate>> getAvailableDays(
      @RequestParam int year, @RequestParam int month) {
    List<LocalDate> availableDays = slotService.getAvailableDaysInMonth(year, month);
    if (availableDays.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(availableDays);
  }
}
