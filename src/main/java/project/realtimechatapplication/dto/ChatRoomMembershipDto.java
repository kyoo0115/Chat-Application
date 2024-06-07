package project.realtimechatapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomMembershipDto {

  private Long chatRoomId;
  private String requestUsername;
  private String ownerUsername;
  private boolean isMemberAlreadyInChatRoom;

}
