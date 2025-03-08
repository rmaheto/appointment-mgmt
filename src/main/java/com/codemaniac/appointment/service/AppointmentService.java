package com.codemaniac.appointment.service;

import com.codemaniac.appointment.dto.AppointmentDto;
import com.codemaniac.appointment.dto.RescheduleRequest;
import com.codemaniac.appointment.entity.*;
import com.codemaniac.appointment.enums.Booked;
import com.codemaniac.appointment.enums.Rescheduled;
import com.codemaniac.appointment.enums.SlotStatus;
import com.codemaniac.appointment.exception.*;
import com.codemaniac.appointment.repository.AppointmentRepository;
import com.codemaniac.appointment.repository.AppointmentSlotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.codemaniac.appointment.repository.UserRepository;
import com.codemaniac.appointment.mapper.AppointmentMapper;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentSlotRepository slotRepository;
  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;
  private final StripePaymentService paymentService;

  /** Get appointment by appointment ID. */
  public AppointmentDto getAppointmentById(final Long appointmentId) {
    final Appointment appointment =
        appointmentRepository
            .findById(appointmentId)
            .orElseThrow(
                () ->
                    new AppointmentNotFoundException(
                        "Appointment not found with ID: " + appointmentId));
    return AppointmentMapper.toDto(appointment);
  }

  /** Get all appointments for a given user. */
  public List<AppointmentDto> getAppointmentsByUser(final String username) {
    final User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new AppointmentNotFoundException("User not found with username: " + username));

    final List<Appointment> appointments = appointmentRepository.findByUser(user);
    return appointments.stream().map(AppointmentMapper::toDto).toList();
  }

  @Transactional
  public Map<String, String> initiateBooking(final Long userId, final Long slotId)
      throws StripeException {
    final User user =
        userRepository
            .getUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    // Fetch slot with PESSIMISTIC LOCK to prevent double booking
    final AppointmentSlot slot =
        slotRepository
            .findByIdForUpdate(slotId)
            .filter(s -> s.getStatus() == SlotStatus.AVAILABLE)
            .orElseThrow(() -> new SlotAlreadyBookedException("Slot is not available"));

    final AppointmentType type = slot.getAppointmentType();

    // If payment is required, create PaymentIntent
    if (type.isRequiresPayment()) {
      slot.setStatus(SlotStatus.PENDING_PAYMENT);
      slotRepository.save(slot);

      final String paymentIntentId =
          paymentService.createPaymentIntent((long) type.getAppointmentFee(), "usd");
      return Map.of("paymentIntentId", paymentIntentId);
    }

    finalizeBooking(user, slot, null);
    return Map.of("status", "BOOKED");
  }

  @Transactional
  public Map<String, String> confirmBooking(
      final Long userId,
      final Long slotId,
      final String paymentIntentId,
      final String paymentMethodId)
      throws StripeException {
    final boolean isPaid = paymentService.confirmAndCharge(paymentIntentId, paymentMethodId);

    final AppointmentSlot slot =
        slotRepository
            .findById(slotId)
            .orElseThrow(() -> new SlotNotFoundException("Slot not found"));

    if (!isPaid) {
      slot.setStatus(SlotStatus.AVAILABLE);
      slotRepository.save(slot);
      throw new PaymentFailedException("Payment was not successful.");
    }

    // Fetch user
    final User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    // Finalize booking
    finalizeBooking(user, slot, paymentIntentId);
    return Map.of("status", "BOOKED");
  }

  private void finalizeBooking(
      final User user, final AppointmentSlot slot, final String paymentIntentId) {
    final Appointment appointment = new Appointment();
    appointment.setUser(user);
    appointment.setAppointmentSlot(slot);
    appointment.setAppointmentType(slot.getAppointmentType());
    appointment.setStatus(new Booked());

    if (paymentIntentId != null) {
      final Payment payment = new Payment();
      payment.setAppointment(appointment);
      payment.setTransactionId(paymentIntentId);
      payment.setAmount(slot.getAppointmentType().getAppointmentFee());
      payment.setStatus("SUCCESS");
      appointment.setPayment(payment);
    }

    slot.setStatus(SlotStatus.BOOKED);
    slotRepository.save(slot);

    appointmentRepository.save(appointment);
  }

  /** Reschedules an appointment to a new available slot. */
  @Transactional
  public void rescheduleAppointment(final RescheduleRequest request) {
    final Appointment appointment =
        appointmentRepository
            .findById(request.appointmentId())
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

    final int maxReschedules = appointment.getAppointmentType().getMaxReschedules();
    if (appointment.getRescheduleCount() >= maxReschedules) {
      throw new IllegalStateException("Maximum reschedules reached for this appointment type!");
    }

    final AppointmentSlot newSlot =
        slotRepository
            .findByIdForUpdate(request.newSlotId())
            .filter(slot -> !slot.getStatus().equals(SlotStatus.BOOKED))
            .orElseThrow(
                () -> new SlotAlreadyBookedException("Slot already booked or not available"));

    // Free the old slot
    appointment.getAppointmentSlot().setStatus(SlotStatus.AVAILABLE);
    slotRepository.save(appointment.getAppointmentSlot());

    // Assign new slot
    newSlot.setStatus(SlotStatus.BOOKED);
    slotRepository.save(newSlot);

    // Update appointment details
    appointment.setAppointmentSlot(newSlot);
    appointment.setStatus(new Rescheduled(LocalDateTime.now()));
    appointment.setRescheduleCount(appointment.getRescheduleCount() + 1);

    appointmentRepository.save(appointment);
  }
}
