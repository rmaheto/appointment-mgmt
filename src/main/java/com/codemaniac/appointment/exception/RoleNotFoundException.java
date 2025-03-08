package com.codemaniac.appointment.exception;

public class RoleNotFoundException extends RuntimeException{

  public RoleNotFoundException(String msg){
    super(msg);
  }
}
