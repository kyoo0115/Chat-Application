package project.realtimechatapplication.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMessageRequestDto {

  private String sender;
  private String roomCode;
}
