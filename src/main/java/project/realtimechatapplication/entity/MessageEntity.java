package project.realtimechatapplication.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.model.type.MessageType;

@Table(name = "message")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity extends TimeStamped {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @Enumerated(EnumType.STRING)
  private MessageType type;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoomEntity chatRoom;

  @OneToMany(mappedBy = "parentMessage", cascade = ALL, fetch = LAZY)
  private List<MessageEntity> replies = new ArrayList<>();

  @OneToMany(mappedBy = "message", cascade = ALL, fetch = LAZY)
  private List<ReactionEntity> reactions = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "parent_message_id")
  private MessageEntity parentMessage;

  @OneToMany(mappedBy = "message", cascade = ALL, fetch = LAZY)
  private List<FileEntity> files = new ArrayList<>();

  public static MessageEntity of(MessageType type, String message, ChatRoomEntity chatRoom,
      UserEntity user) {
    return MessageEntity.builder()
        .type(type)
        .message(message)
        .chatRoom(chatRoom)
        .user(user)
        .build();
  }
}
