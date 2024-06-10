package project.realtimechatapplication.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserRequestDto {
  
  @NotBlank(message = "Name cannot be blank.")
  private String name;
}
