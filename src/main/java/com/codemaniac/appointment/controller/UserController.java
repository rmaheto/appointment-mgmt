package com.codemaniac.appointment.controller;

import java.util.List;
import java.util.Set;

import com.codemaniac.appointment.dto.UserRequestDto;
import com.codemaniac.appointment.dto.UserResponseDto;
import com.codemaniac.appointment.entity.Role;
import com.codemaniac.appointment.enums.RoleName;
import com.codemaniac.appointment.model.UpdatePasswordRequest;
import com.codemaniac.appointment.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    UserResponseDto user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/create")
  public ResponseEntity<String> createUser(@RequestBody UserRequestDto userDto) {
    userService.createUser(userDto);
    return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
  }

  @PreAuthorize("authentication.principal.id == #userId or hasRole('ROLE_ADMIN')")
  @PatchMapping("/{userId}/update-password")
  public ResponseEntity<String> updatePassword(
      @PathVariable Long userId, @RequestBody UpdatePasswordRequest passwordRequest) {
    userService.updatePassword(userId, passwordRequest);
    return ResponseEntity.ok("Password updated successfully");
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PatchMapping("/users/{userId}/roles")
  public ResponseEntity<UserResponseDto> updateUserRoles(
      @PathVariable Long userId, @RequestBody Set<RoleName> newRoles) {

    return new ResponseEntity<>(userService.updateUserRoles(userId, newRoles), HttpStatus.OK);
  }

  @GetMapping("/role/{roleName}")
  public ResponseEntity<List<UserResponseDto>> getUsersByRole(@PathVariable String roleName) {

    return ResponseEntity.ok(userService.getUsersByRole(roleName));
  }

  @GetMapping("/roles")
  public ResponseEntity<List<Role>> getAllRoles() {
    return new ResponseEntity<>(userService.getAllRoles(), HttpStatus.OK);
  }
}
