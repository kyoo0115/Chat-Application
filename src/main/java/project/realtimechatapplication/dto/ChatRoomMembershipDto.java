package project.realtimechatapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomMembershipDto {

  private Long chatRoomId;
  private String requestUsername;
  private String ownerUsername;
  private boolean isMemberAlreadyInChatRoom;

}
