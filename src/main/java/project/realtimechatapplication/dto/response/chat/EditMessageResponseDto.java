package project.realtimechatapplication.dto.response.chat;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import project.realtimechatapplication.model.type.MessageStatus;

@Getter
@Builder
public class EditMessageResponseDto {

  private Long messageId;
  private String newMessage;
  private String senderId;
  private Long chatRoomId;
  private LocalDateTime timestamp;
  private MessageStatus status;
}
