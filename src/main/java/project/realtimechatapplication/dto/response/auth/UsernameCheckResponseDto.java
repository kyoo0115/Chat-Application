package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;

public class UsernameCheckResponseDto extends ResponseDto {

  private UsernameCheckResponseDto() {
    super();
  }

  public static ResponseEntity<? super UsernameCheckResponseDto> success() {
    ResponseDto responseBody = new UsernameCheckResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }
}
