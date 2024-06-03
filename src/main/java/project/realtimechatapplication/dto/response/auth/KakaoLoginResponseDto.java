package project.realtimechatapplication.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginResponseDto {

  private String token;
  private String username;
  private String email;
}
