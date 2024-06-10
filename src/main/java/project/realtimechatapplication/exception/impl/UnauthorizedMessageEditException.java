package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class UnauthorizedMessageEditException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.FORBIDDEN.value();
  }

  @Override
  public String getMessage() {
    return "You are not authorized to edit this message.";
  }
}
