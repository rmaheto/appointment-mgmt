package com.codemaniac.appointment.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.codemaniac.appointment.dto.UserRequestDto;
import com.codemaniac.appointment.dto.UserResponseDto;
import com.codemaniac.appointment.entity.Role;
import com.codemaniac.appointment.entity.User;
import com.codemaniac.appointment.enums.RoleName;
import com.codemaniac.appointment.exception.IncorrectOldPasswordException;
import com.codemaniac.appointment.exception.PasswordMismatchException;
import com.codemaniac.appointment.exception.RoleNotFoundException;
import com.codemaniac.appointment.exception.UserNotFoundException;
import com.codemaniac.appointment.model.UpdatePasswordRequest;
import com.codemaniac.appointment.repository.RoleRepository;
import com.codemaniac.appointment.repository.UserRepository;
import com.codemaniac.appointment.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String USER_NOT_FOUND = "User not found";
  private static final String ROLE_NOT_FOUND = "Role not found";

  public void createUser(final UserRequestDto userDto) {

    final String encodedPassword = passwordEncoder.encode(userDto.getPassword());

    final User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(encodedPassword);

    final Set<Role> roles = new HashSet<>();
    for (final RoleName roleName : userDto.getRoles()) {
      final Role role =
          roleRepository
              .findByName(roleName)
              .orElseThrow(() -> new RoleNotFoundException(ROLE_NOT_FOUND));
      roles.add(role);
    }
    user.setRoles(roles);

    userRepository.save(user);
  }

  public void updatePassword(final Long userId, final UpdatePasswordRequest passwordRequest) {
    final User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

    if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
      throw new IncorrectOldPasswordException("Old password is incorrect");
    }

    if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
      throw new PasswordMismatchException("New password and confirm password do not match");
    }

    final String encodedNewPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
    user.setPassword(encodedNewPassword);

    userRepository.save(user);
  }

  public List<UserResponseDto> getAllUsers() {
    final List<User> users = userRepository.findAll();
    return users.stream().map(UserMapper::toUserResponseDto).toList();
  }

  public UserResponseDto getUserById(final Long userId) {
    final User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    return UserMapper.toUserResponseDto(user);
  }

  public UserResponseDto updateUserRoles(final Long userId, final Set<RoleName> newRoles) {
    final User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

    final Set<Role> roles = new HashSet<>();
    for (final RoleName roleName : newRoles) {
      final Role role =
          roleRepository
              .findByName(roleName)
              .orElseThrow(() -> new RoleNotFoundException(ROLE_NOT_FOUND));
      roles.add(role);
    }

    user.setRoles(roles);
    final User updatedUser = userRepository.save(user);

    return UserMapper.toUserResponseDto(updatedUser);
  }

  public List<UserResponseDto> getUsersByRole(final String roleName) {
    final RoleName roleEnum;
    try {
      roleEnum = RoleName.valueOf(roleName);
    } catch (IllegalArgumentException e) {
      throw new RoleNotFoundException("Invalid role: " + roleName);
    }

    final List<User> users = userRepository.findByRolesName(roleEnum);

    return users.stream().map(UserMapper::toUserResponseDto).toList();
  }

  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }
}
