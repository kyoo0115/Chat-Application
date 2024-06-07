package project.realtimechatapplication.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageRequestDto {

  private String roomCode;
  private String sender;
  private String message;
}
