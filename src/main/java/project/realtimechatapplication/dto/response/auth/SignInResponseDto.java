package project.realtimechatapplication.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignInResponseDto {

  @JsonProperty("token")
  private final String token;

  @JsonProperty("expirationTime")
  private final int expirationTime;

  @JsonProperty("username")
  private final String username;

  public SignInResponseDto(String token, String username) {

    this.token = token;
    this.expirationTime = 3600;
    this.username = username;
  }
}
