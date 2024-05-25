package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import project.realtimechatapplication.dto.response.ResponseDto;

@NoArgsConstructor
public class UsernameCheckResponseDto extends ResponseDto {

  public static ResponseEntity<? super UsernameCheckResponseDto> success() {
    ResponseDto responseBody = new UsernameCheckResponseDto();
    return ResponseEntity.status(OK).body(responseBody);
  }
}
