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
public class AddMemberNotificationResponseDto {

  private Long id;
  private NotificationType type;
  private Long userId;
  private String message;

  public static AddMemberNotificationResponseDto to(NotificationEntity notificationEntity,
      String chatRoomName) {
    return AddMemberNotificationResponseDto.builder()
        .id(notificationEntity.getId())
        .type(NotificationType.INVITATION)
        .userId(notificationEntity.getUser().getId())
        .message(chatRoomName + " added " + notificationEntity.getUser().getUsername())
        .build();
  }
}
