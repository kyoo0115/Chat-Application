package project.realtimechatapplication.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendRequestDto {
  private String message;
  private String senderId;
}
