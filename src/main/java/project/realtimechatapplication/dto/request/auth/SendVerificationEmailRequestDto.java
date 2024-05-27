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

  @NotBlank(message = "Username cannot be blank.")
  private String username;

  @NotBlank(message = "Email cannot be blank.")
  @Email(message = "Invalid email format.")
  private String email;
}
