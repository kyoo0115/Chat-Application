package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomAddRequestDto {

  @NotBlank(message = "Invalid username: empty name")
  private String username;

  @NotBlank(message = "Invalid room code: empty room code")
  private String roomCode;
  private boolean isPrivate;
}
