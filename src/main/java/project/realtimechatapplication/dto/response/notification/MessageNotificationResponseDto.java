package project.realtimechatapplication.dto.response.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.entity.NotificationEntity;
import project.realtimechatapplication.model.type.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageNotificationResponseDto {

  private Long id;
  private NotificationType type;
  private Long userId;
  private Long messageId;
  private String message;

  public static MessageNotificationResponseDto to(NotificationEntity notificationEntity,
      String chatRoomName, String senderUsername, String messageBody) {
    return MessageNotificationResponseDto.builder()
        .id(notificationEntity.getId())
        .type(NotificationType.MESSAGE)
        .userId(notificationEntity.getUser().getId())
        .messageId(notificationEntity.getMessage() != null ? notificationEntity.getMessage().getId()
            : null)
        .message(senderUsername + " has sent a message in " + chatRoomName + ": " + messageBody)
        .build();
  }
}
