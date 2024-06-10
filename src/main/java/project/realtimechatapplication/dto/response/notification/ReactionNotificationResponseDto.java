package project.realtimechatapplication.dto.response.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.entity.NotificationEntity;
import project.realtimechatapplication.model.type.NotificationType;
import project.realtimechatapplication.model.type.Reaction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionNotificationResponseDto {
  private Long id;
  private NotificationType type;
  private Long userId;
  private Long messageId;
  private String message;

  public static ReactionNotificationResponseDto to(NotificationEntity notificationEntity, String senderUsername, Reaction reactionType) {
    return ReactionNotificationResponseDto.builder()
        .id(notificationEntity.getId())
        .type(NotificationType.REACTION)
        .userId(notificationEntity.getUser().getId())
        .messageId(notificationEntity.getMessage().getId())
        .message(senderUsername + " reacted with " + reactionType + " with your message")
        .build();
  }
}
