package com.codemaniac.appointment.mapper;

import com.codemaniac.appointment.dto.UserResponseDto;
import com.codemaniac.appointment.entity.Role;
import com.codemaniac.appointment.entity.User;

import java.util.Set;
import java.util.stream.Collectors;


public class UserMapper {

  private UserMapper(){}

  public static UserResponseDto toUserResponseDto(User user) {
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setId(user.getId());
    userResponseDto.setUsername(user.getUsername());
    userResponseDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    return userResponseDto;
  }
  
  public static User toEntity(UserResponseDto userDto, Set<Role> roles) {
    User user = new User();
    user.setId(userDto.getId());
    user.setUsername(userDto.getUsername());
    user.setRoles(roles);
    return user;
  }
}
