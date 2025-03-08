package com.codemaniac.appointment.repository;

import com.codemaniac.appointment.entity.Appointment;
import java.util.List;
import java.util.Optional;

import com.codemaniac.appointment.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  @Nonnull
  Optional<Appointment> findById(@Nonnull final Long id);

  List<Appointment> findByUser(User user);
}
