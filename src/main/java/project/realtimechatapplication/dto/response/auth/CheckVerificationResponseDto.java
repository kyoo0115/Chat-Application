package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;

public class CheckVerificationResponseDto extends ResponseDto {

  private CheckVerificationResponseDto() {
    super();
  }

  public static ResponseEntity<? super CheckVerificationResponseDto> success() {
    ResponseDto responseBody = new CheckVerificationResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }
}


