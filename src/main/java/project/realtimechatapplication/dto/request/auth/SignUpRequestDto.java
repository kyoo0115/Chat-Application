package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

  @NotBlank(message = "Username cannot be blank.")
  private String username;

  @NotBlank(message = "Name cannot be blank.")
  private String name;

  @NotBlank(message = "Password cannot be blank.")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$", message = "Invalid Format Password")
  private String password;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email cannot be blank.")
  private String email;

  @NotBlank(message = "Verification number cannot be blank.")
  @Length(min = 4, max = 4, message = "Invalid verification number: length should be 4")
  private String verificationNumber;

  @Past(message = "Invalid Birth Date, should be YYYY-MM-DD format")
  private LocalDate birthDate;

}
