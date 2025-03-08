package com.codemaniac.appointment.repository;

import java.util.List;
import java.util.Optional;

import com.codemaniac.appointment.entity.User;
import com.codemaniac.appointment.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> getUserById(Long id);

  List<User> findByRolesName(RoleName roleName);
}
