package com.codemaniac.appointment.repository;

import com.codemaniac.appointment.entity.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {
  boolean existsByName(String name);
}
