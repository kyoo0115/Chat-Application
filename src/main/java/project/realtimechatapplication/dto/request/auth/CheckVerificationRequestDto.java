package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class CheckVerificationRequestDto {

  @NotBlank(message = "Invalid username: empty name")
  private String username;

  @NotBlank(message = "Invalid email: empty name")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Invalid verification number: empty name")
  @Length(min = 4, max = 4, message = "Invalid verification number: length should be 4")
  private String verificationNumber;
}
