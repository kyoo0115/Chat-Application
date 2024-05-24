package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

  @NotBlank
  private String username;

  @NotBlank
  private String name;

  @NotBlank
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
  private String password;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String verificationNumber;

  @Past
  private LocalDate birthDate;

}
