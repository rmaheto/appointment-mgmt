package com.codemaniac.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
  private String oldPassword;
  private String newPassword;
  private String confirmPassword;
}
