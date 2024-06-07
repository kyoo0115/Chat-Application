package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class MemberAlreadyInChatroomException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "Member to be added is already in the chatroom.";
  }
}
