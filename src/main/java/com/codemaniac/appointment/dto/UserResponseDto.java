package com.codemaniac.appointment.dto;

import java.util.Set;

import com.codemaniac.appointment.enums.RoleName;
import lombok.Data;

@Data
public class UserResponseDto {
  private Long id;
  private String username;
  private Set<RoleName> roles;
}
