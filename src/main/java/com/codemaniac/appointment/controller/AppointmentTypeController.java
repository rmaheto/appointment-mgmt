package com.codemaniac.appointment.controller;

import com.codemaniac.appointment.dto.AppointmentTypeRequest;
import com.codemaniac.appointment.entity.AppointmentType;
import com.codemaniac.appointment.service.AppointmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-types")
@RequiredArgsConstructor
public class AppointmentTypeController {

  private final AppointmentTypeService service;

  @PostMapping
  public ResponseEntity<AppointmentType> createAppointmentType(
      @RequestBody AppointmentTypeRequest type) {
    AppointmentType createdType = service.createAppointmentType(type);
    return ResponseEntity.ok(createdType);
  }

  @GetMapping
  public ResponseEntity<List<AppointmentType>> getAllAppointmentTypes() {
    List<AppointmentType> types = service.getAllAppointmentTypes();
    return ResponseEntity.ok(types);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentType> getAppointmentTypeById(@PathVariable Long id) {
    AppointmentType type = service.getAppointmentTypeById(id);
    return ResponseEntity.ok(type);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AppointmentType> updateAppointmentType(
      @PathVariable Long id, @RequestBody AppointmentType updatedType) {
    AppointmentType updated = service.updateAppointmentType(id, updatedType);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAppointmentType(@PathVariable Long id) {
    service.deleteAppointmentType(id);
    return ResponseEntity.ok("Appointment type deleted successfully.");
  }
}
