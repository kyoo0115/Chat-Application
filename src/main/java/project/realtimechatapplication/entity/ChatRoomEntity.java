package project.realtimechatapplication.entity;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomEntity extends TimeStamped {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean isPrivate;

  @Column(nullable = false, unique = true)
  private String roomCode;

  @Column(nullable = false)
  private String owner;

  @OneToMany(mappedBy = "chatRoom", cascade = REMOVE, fetch = LAZY, orphanRemoval = true)
  private List<MessageEntity> messages = new ArrayList<>();

  @OneToMany(mappedBy = "chatRoom", cascade = REMOVE, fetch = LAZY, orphanRemoval = true)
  private List<MemberChatRoomEntity> memberChatRooms = new ArrayList<>();

  private ChatRoomEntity(String roomName, String owner) {
    this.roomCode = UUID.randomUUID().toString();
    this.name = roomName;
    this.owner = owner;
  }

  public static ChatRoomEntity of(String roomName, String owner) {
    return new ChatRoomEntity(roomName, owner);
  }
}
