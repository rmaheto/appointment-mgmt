package com.codemaniac.appointment.controller;

import com.codemaniac.appointment.dto.AppointmentDto;
import com.codemaniac.appointment.dto.RescheduleRequest;
import com.codemaniac.appointment.service.AppointmentService;
import com.stripe.exception.StripeException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;

  @PostMapping("/initiate-booking")
  public ResponseEntity<Map<String, String>> initiateBooking(
      @RequestParam final Long userId, @RequestParam final Long slotId) {
    try {
      final Map<String, String> response = appointmentService.initiateBooking(userId, slotId);
      return ResponseEntity.ok(response);
    } catch (final Exception e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @PostMapping("/confirm-booking")
  public ResponseEntity<Map<String, String>> confirmBooking(
      @RequestParam final Long userId,
      @RequestParam final Long slotId,
      @RequestParam final String paymentIntentId,
      @RequestParam final String paymentMethodId) {
    try {
      final Map<String, String> response =
          appointmentService.confirmBooking(userId, slotId, paymentIntentId, paymentMethodId);
      return ResponseEntity.ok(response);
    } catch (final StripeException e) {
      return ResponseEntity.status(402).body(Map.of("error", "Payment failed: " + e.getMessage()));
    } catch (final Exception e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  @GetMapping("/{appointmentId}")
  public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable final Long appointmentId) {
    try {
      final AppointmentDto appointment = appointmentService.getAppointmentById(appointmentId);
      return ResponseEntity.ok(appointment);
    } catch (final Exception e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<List<AppointmentDto>> getAppointmentsByUser(
      @PathVariable final String username) {
    try {
      final List<AppointmentDto> appointments = appointmentService.getAppointmentsByUser(username);

      return appointments.isEmpty()
          ? ResponseEntity.noContent().build()
          : ResponseEntity.ok(appointments);
    } catch (final Exception e) {
      return ResponseEntity.badRequest().body(Collections.emptyList());
    }
  }

  @PostMapping("/reschedule")
  public ResponseEntity<Map<String, String>> rescheduleAppointment(
      @RequestBody final RescheduleRequest request) {
    try {
      appointmentService.rescheduleAppointment(request);
      return ResponseEntity.ok(Map.of("message", "Appointment successfully rescheduled"));
    } catch (final Exception e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}
