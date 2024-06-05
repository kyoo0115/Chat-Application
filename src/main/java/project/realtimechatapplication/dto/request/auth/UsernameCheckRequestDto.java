package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameCheckRequestDto {

  @NotBlank(message = "Invalid username: empty name")
  private String username;
}