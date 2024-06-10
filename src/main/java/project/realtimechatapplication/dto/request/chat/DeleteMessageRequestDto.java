package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMessageRequestDto {

  @NotBlank(message = "Sender cannot be blank.")
  private String sender;

  @NotBlank(message = "Room code cannot be blank.")
  private String roomCode;
}
