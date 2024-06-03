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

  @JsonProperty("username")
  private final String username;

  private SignInResponseDto(String token, String username) {
    super();
    this.token = token;
    this.expirationTime = 3600;
    this.username = username;
  }

  public static ResponseEntity<SignInResponseDto> authenticate(String token, String username) {
    SignInResponseDto responseBody = new SignInResponseDto(token, username);
    return ResponseEntity.status(OK).body(responseBody);
  }
}
