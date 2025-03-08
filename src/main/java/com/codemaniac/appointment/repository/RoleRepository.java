package com.codemaniac.appointment.repository;

import java.util.Optional;

import com.codemaniac.appointment.entity.Role;
import com.codemaniac.appointment.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);

  boolean existsByName(RoleName roleName);
}
