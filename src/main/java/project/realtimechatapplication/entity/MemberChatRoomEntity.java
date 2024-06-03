package project.realtimechatapplication.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_chat_room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberChatRoomEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoomEntity chatRoom;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  public static MemberChatRoomEntity of(ChatRoomEntity chatRoom, UserEntity user) {
    MemberChatRoomEntity memberChatRoom = new MemberChatRoomEntity();
    memberChatRoom.setChatRoom(chatRoom);
    memberChatRoom.setUser(user);
    return memberChatRoom;
  }
}
