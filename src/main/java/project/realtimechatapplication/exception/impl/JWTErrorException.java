package project.realtimechatapplication.exception.impl;

import org.springframework.http.HttpStatus;
import project.realtimechatapplication.exception.AbstractException;

public class JWTErrorException extends AbstractException {


  @Override
  public int getStatusCode() {
    return HttpStatus.UNAUTHORIZED.value();
  }

  @Override
  public String getMessage() {
    return "JWT token is not valid.";
  }
}
