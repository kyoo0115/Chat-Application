package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageRequestDto {

  @NotBlank(message = "Room code cannot be blank.")
  private String roomCode;

  @NotBlank(message = "Sender cannot be blank.")
  private String sender;

  @NotBlank(message = "Message cannot be blank.")
  private String message;
}
