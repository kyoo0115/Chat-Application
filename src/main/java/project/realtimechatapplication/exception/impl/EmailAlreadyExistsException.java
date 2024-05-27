package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class EmailAlreadyExistsException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.CONFLICT.value();
  }

  @Override
  public String getMessage() {
    return "Email already exists.";
  }
}
