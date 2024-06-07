package project.realtimechatapplication.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(AbstractException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(e.getStatusCode())
        .message(e.getMessage())
        .build();

    log.error("Exception: ", e);
    return new ResponseEntity<>(errorResponse, new HttpHeaders(),
        HttpStatus.valueOf(e.getStatusCode()));
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    if (e instanceof AbstractException) {
      status = HttpStatus.valueOf(((AbstractException) e).getStatusCode());
    }

    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(status.value())
        .message(e.getMessage())
        .build();

    log.error("General exception: ", e);
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), status);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());

    log.error("Validation exception: ", ex);
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, List<String>>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    List<String> errors = List.of("Malformed JSON request");

    log.error("Malformed JSON request: ", ex);
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  private Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }
}
