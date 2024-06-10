package project.realtimechatapplication.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.entity.ChatRoomEntity;

@Getter
@Setter
@Builder
public class ChatRoomDto {

  @NotBlank(message = "id cannot be blank.")
  private Long id;

  @NotBlank(message = "Room code cannot be blank.")
  private String roomCode;

  @NotBlank(message = "Room name cannot be blank.")
  private String roomName;

  public static ChatRoomDto from(ChatRoomEntity entity) {
    return ChatRoomDto.builder()
        .id(entity.getId())
        .roomCode(entity.getRoomCode())
        .roomName(entity.getName())
        .build();
  }
}
