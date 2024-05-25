package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;

public class SendVerificationEmailResponseDto extends ResponseDto {

  private SendVerificationEmailResponseDto() {
    super();
  }

  public static ResponseEntity<? super SendVerificationEmailResponseDto> success() {
    SendVerificationEmailResponseDto responseBody = new SendVerificationEmailResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }
}
