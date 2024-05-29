package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDto {

  @NotBlank(message = "Username cannot be blank.")
  private String username;

  @NotBlank(message = "Password cannot be blank.")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$", message = "Invalid Format Password")
  private String password;
}
