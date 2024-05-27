package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class EmailSendErrorException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  @Override
  public String getMessage() {
    return "Email send failure.";
  }
}
