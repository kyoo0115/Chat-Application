package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;

public class SignUpResponseDto extends ResponseDto {

  private SignUpResponseDto() {
    super();
  }

  public static ResponseEntity<SignUpResponseDto> success() {
    SignUpResponseDto responseBody = new SignUpResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }
}
