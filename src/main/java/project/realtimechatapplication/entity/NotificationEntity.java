package project.realtimechatapplication.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.realtimechatapplication.model.type.NotificationType;

@Table(name = "notification")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity extends TimeStamped {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "message_id")
  private MessageEntity message;

  public static NotificationEntity notification(NotificationType type, UserEntity user,
      MessageEntity message) {
    return NotificationEntity.builder()
        .type(type)
        .user(user)
        .message(message)
        .build();
  }

  public static NotificationEntity notification(NotificationType type, UserEntity user) {
    return NotificationEntity.builder()
        .type(type)
        .user(user)
        .build();
  }
}

