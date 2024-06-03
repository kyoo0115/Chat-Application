package project.realtimechatapplication.dto.request.chat;

import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.model.type.MessageType;

@Getter
@Setter
public class ChatDto {
  private String roomCode;
  private String message;
  private String sender;
  private MessageType type;
}
