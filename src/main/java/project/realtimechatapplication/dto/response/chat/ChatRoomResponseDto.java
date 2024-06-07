package project.realtimechatapplication.dto.response.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDto {

  private Long id;
  private String name;
  private Boolean isPrivate;
}
