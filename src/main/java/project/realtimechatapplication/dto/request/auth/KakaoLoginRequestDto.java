package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginRequestDto {

  @NotBlank
  private String authorizationCode;
}
