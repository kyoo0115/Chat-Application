package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomAddRequestDto {

  @NotBlank(message = "Room code cannot be blank.")
  private String roomCode;
  private boolean isPrivate;
}
