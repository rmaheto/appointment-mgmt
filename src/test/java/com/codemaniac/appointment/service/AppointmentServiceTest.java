package com.codemaniac.appointment.service;

import com.codemaniac.appointment.dto.AppointmentDto;
import com.codemaniac.appointment.dto.RescheduleRequest;
import com.codemaniac.appointment.entity.Appointment;
import com.codemaniac.appointment.entity.AppointmentSlot;
import com.codemaniac.appointment.entity.AppointmentType;
import com.codemaniac.appointment.entity.User;
import com.codemaniac.appointment.enums.SlotStatus;
import com.codemaniac.appointment.repository.AppointmentRepository;
import com.codemaniac.appointment.repository.AppointmentSlotRepository;
import com.codemaniac.appointment.repository.UserRepository;
import com.stripe.exception.StripeException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

  @InjectMocks
  private AppointmentService appointmentService;

  @Mock
  private AppointmentRepository appointmentRepository;
  @Mock
  private AppointmentSlotRepository slotRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private StripePaymentService paymentService;


  @Test
  void getAppointmentById_whenFound_shouldReturnDto() {
    final Appointment appointment = new Appointment();
    appointment.setId(1L);
    when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

    final AppointmentDto dto = appointmentService.getAppointmentById(1L);

    assertThat (dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(1L);
  }

  @Test
  void getAppointmentsByUser_whenUserExists_shouldReturnList() {
    final User user = new User();
    user.setUsername("john");
    when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

    final Appointment appointment = new Appointment();
    appointment.setId(1L);
    when(appointmentRepository.findByUser(user)).thenReturn(List.of(appointment));

    final List<AppointmentDto> result = appointmentService.getAppointmentsByUser("john");

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(1L);
  }

  @Test
  void initiateBooking_withoutPayment_shouldFinalizeAndReturnBooked() throws StripeException {
    final long userId = 1L;
    final long slotId = 2L;

    final User user = new User();
    final AppointmentType type = new AppointmentType();
    type.setRequiresPayment(false);

    final AppointmentSlot slot = new AppointmentSlot();
    slot.setId(slotId);
    slot.setStatus(SlotStatus.AVAILABLE);
    slot.setAppointmentType(type);

    when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
    when(slotRepository.findByIdForUpdate(slotId)).thenReturn(Optional.of(slot));

    final Map<String, String> response = appointmentService.initiateBooking(userId, slotId);

    assertThat(response).containsEntry("status", "BOOKED");
    verify(slotRepository).save(slot);
    verify(appointmentRepository).save(any(Appointment.class));
  }

  @Test
  void confirmBooking_whenPaymentSuccessful_shouldFinalizeBooking() throws StripeException {
    final long userId = 1L;
    final long slotId = 2L;
    final String intentId = "pi_123";
    final String methodId = "pm_123";

    final AppointmentSlot slot = new AppointmentSlot();
    final AppointmentType type = new AppointmentType();
    type.setAppointmentFee(100);
    slot.setAppointmentType(type);

    final User user = new User();

    when(paymentService.confirmAndCharge(intentId, methodId)).thenReturn(true);
    when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    final Map<String, String> result = appointmentService.confirmBooking(userId, slotId, intentId, methodId);

    assertThat(result).containsEntry("status", "BOOKED");
    verify(slotRepository).save(slot);
    verify(appointmentRepository).save(any(Appointment.class));
  }

  @Test
  void rescheduleAppointment_withValidConditions_shouldUpdateAppointment() {
    final long oldSlotId = 1L;
    final long newSlotId = 2L;

    final AppointmentSlot oldSlot = new AppointmentSlot();
    oldSlot.setId(oldSlotId);
    oldSlot.setStatus(SlotStatus.BOOKED);

    final AppointmentType type = new AppointmentType();
    type.setMaxReschedules(5);

    final Appointment appointment = new Appointment();
    appointment.setAppointmentSlot(oldSlot);
    appointment.setAppointmentType(type);
    appointment.setRescheduleCount(1);

    final AppointmentSlot newSlot = new AppointmentSlot();
    newSlot.setId(newSlotId);
    newSlot.setStatus(SlotStatus.AVAILABLE);

    final RescheduleRequest request = new RescheduleRequest(100L, newSlotId);

    when(appointmentRepository.findById(request.appointmentId())).thenReturn(Optional.of(appointment));
    when(slotRepository.findByIdForUpdate(newSlotId)).thenReturn(Optional.of(newSlot));

    appointmentService.rescheduleAppointment(request);

    assertThat(appointment.getAppointmentSlot()).isEqualTo(newSlot);
    verify(slotRepository, times(2)).save(any(AppointmentSlot.class));
    verify(appointmentRepository).save(appointment);
  }
}
