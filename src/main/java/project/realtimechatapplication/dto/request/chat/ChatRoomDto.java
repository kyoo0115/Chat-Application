package project.realtimechatapplication.dto.request.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.entity.ChatRoomEntity;

@Getter
@Setter
@Builder
public class ChatRoomDto {
  private Long id;
  private String roomCode;
  private String roomName;

  public static ChatRoomDto from(ChatRoomEntity entity) {
    return ChatRoomDto.builder()
        .id(entity.getId())
        .roomCode(entity.getRoomCode())
        .roomName(entity.getName())
        .build();
  }
}
