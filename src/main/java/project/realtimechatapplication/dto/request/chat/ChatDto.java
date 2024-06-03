package project.realtimechatapplication.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.model.type.MessageType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

  private MessageType type;
  private String nickname;
  private String email;
  private String roomCode;
  private String message;
  private String imageUrl;

  public static ChatDto from(MessageEntity message) {

    return new ChatDto(
        message.getType(),
        message.getNickname(),
        message.getEmail(),
        message.getRoomCode(),
        message.getMessage()
    );
  }

  public static MessageEntity toEntity(ChatDto chatDto) {
    return MessageEntity.of(
        chatDto.getType(),
        chatDto.getNickname(),
        chatDto.getEmail(),
        chatDto.getRoomCode(),
        chatDto.getMessage()
    );
  }
}