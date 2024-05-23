package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;
import project.realtimechatapplication.model.ResponseCode;
import project.realtimechatapplication.model.ResponseMessage;

public class UsernameCheckResponseDto extends ResponseDto {

  public static ResponseEntity<UsernameCheckResponseDto> success() {
    UsernameCheckResponseDto responseBody = new UsernameCheckResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }

  public static ResponseEntity<ResponseDto> duplicateUsername() {
    ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
    return ResponseEntity.status(CONFLICT).body(responseBody);
  }
}
