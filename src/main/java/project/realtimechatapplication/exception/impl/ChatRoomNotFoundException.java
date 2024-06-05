package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class ChatRoomNotFoundException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.NOT_FOUND.value();
  }

  @Override
  public String getMessage() {
    return "Chat room not found.";
  }
}
