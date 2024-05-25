package project.realtimechatapplication.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Table(name = "message")
@Entity
@Getter
public class MessageEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

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

}
