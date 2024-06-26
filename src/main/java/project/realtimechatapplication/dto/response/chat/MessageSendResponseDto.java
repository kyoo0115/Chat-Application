package project.realtimechatapplication.dto.response.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSendResponseDto {

  private Long messageId;
  private String message;
  private String senderId;
  private Long chatRoomId;
  private LocalDateTime timestamp;
}
