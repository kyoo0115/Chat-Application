package project.realtimechatapplication.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomAddRequestDto {
  private String username;
  private String roomCode;
  private boolean isPrivate;
}
