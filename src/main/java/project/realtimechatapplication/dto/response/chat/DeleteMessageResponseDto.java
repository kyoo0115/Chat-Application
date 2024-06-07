package project.realtimechatapplication.dto.response.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.model.type.MessageStatus;

@Builder
@Getter
@Setter
public class DeleteMessageResponseDto {

  private Long id;
  private String roomCode;
  private MessageStatus status;
}
