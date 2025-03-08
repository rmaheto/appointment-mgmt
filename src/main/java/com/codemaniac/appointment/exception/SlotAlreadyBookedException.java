package com.codemaniac.appointment.exception;

public class SlotAlreadyBookedException extends RuntimeException{

  public SlotAlreadyBookedException(String msg){
    super(msg);
  }

}
