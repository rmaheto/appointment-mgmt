package com.codemaniac.appointment.repository;

import com.codemaniac.appointment.entity.AppointmentSlot;
import com.codemaniac.appointment.enums.SlotStatus;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

  // Fetch all available slots for a specific date
  List<AppointmentSlot> findByDateAndStatus(LocalDate date, SlotStatus status);

  // Fetch all available slots for a specific month
  @Query(
      "SELECT DISTINCT s.date FROM AppointmentSlot s WHERE FUNCTION('MONTH', s.date) = :month "
          + "AND FUNCTION('YEAR', s.date) = :year AND s.status = 'AVAILABLE'")
  List<LocalDate> findAvailableDaysInMonth(@Param("month") int month, @Param("year") int year);

  // Fetch a slot using pessimistic locking to prevent race conditions
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM AppointmentSlot s WHERE s.id = :id")
  Optional<AppointmentSlot> findByIdForUpdate(@Param("id") Long id);

  // Check if slots exist within the given date range
  boolean existsByDateBetween(LocalDate startDate, LocalDate endDate);

  // Check if any slot is booked within the given date range
  boolean existsByDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, SlotStatus status);

  // Fetch all slots within a given date range
  List<AppointmentSlot> findByDateBetween(LocalDate startDate, LocalDate endDate);

  List<AppointmentSlot> findByDateAndStatusAndAppointmentTypeId(
      LocalDate date, SlotStatus status, Long appointmentTypeId);

  @Query(
      "SELECT DISTINCT s.date FROM AppointmentSlot s WHERE MONTH(s.date) = :month AND YEAR(s.date) = :year AND s.appointmentType.id = :appointmentTypeId AND s.status = 'AVAILABLE'")
  List<LocalDate> findAvailableDaysInMonthAndType(
      @Param("month") int month,
      @Param("year") int year,
      @Param("appointmentTypeId") Long appointmentTypeId);
}
