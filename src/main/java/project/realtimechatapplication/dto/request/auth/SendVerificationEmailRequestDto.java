package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendVerificationEmailRequestDto {

  @NotBlank(message = "Invalid username: empty name")
  private String username;

  @NotBlank(message = "Invalid email: empty name")
  @Email(message = "Invalid email format")
  private String email;
}
