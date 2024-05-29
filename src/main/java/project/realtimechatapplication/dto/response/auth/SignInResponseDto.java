package project.realtimechatapplication.dto.response.auth;

import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.response.ResponseDto;

@Getter
public class SignInResponseDto extends ResponseDto {

  @JsonProperty("token")
  private final String token;

  @JsonProperty("expirationTime")
  private final int expirationTime;

  private SignInResponseDto(String token) {
    super();
    this.token = token;
    this.expirationTime = 3600;
  }

  public static ResponseEntity<SignInResponseDto> authenticate(String token) {
    SignInResponseDto responseBody = new SignInResponseDto(token);
    return ResponseEntity.status(OK).body(responseBody);
  }
}
