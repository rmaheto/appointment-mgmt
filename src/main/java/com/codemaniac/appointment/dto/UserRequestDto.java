package com.codemaniac.appointment.dto;

import java.util.Set;

import com.codemaniac.appointment.enums.RoleName;
import lombok.Data;


@Data
public class UserRequestDto {
  private String username;
  private String password;
  private Set<RoleName> roles;
}
