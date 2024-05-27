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

  @NotBlank(message = "Username cannot be blank.")
  private String username;

  @NotBlank(message = "Email cannot be blank.")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Verification number cannot be blank.")
  @Length(min = 4, max = 4, message = "Invalid verification number: length should be 4")
  private String verificationNumber;
}
