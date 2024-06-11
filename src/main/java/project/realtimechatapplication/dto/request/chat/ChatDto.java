package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.model.type.MessageType;

@Getter
@Setter
public class ChatDto {

  @NotBlank(message = "Invalid room code: empty room code")
  private String roomCode;

  @NotBlank(message = "Invalid message: empty message")
  private String message;

  @NotBlank(message = "Invalid sender: empty sender")
  private String sender;

  @NotNull(message = "Invalid type: empty type")
  private MessageType type;
}
