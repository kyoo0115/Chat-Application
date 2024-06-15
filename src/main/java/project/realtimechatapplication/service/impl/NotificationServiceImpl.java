package project.realtimechatapplication.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.dto.response.notification.AddMemberNotificationResponseDto;
import project.realtimechatapplication.dto.response.notification.MessageNotificationResponseDto;
import project.realtimechatapplication.dto.response.notification.ReactionNotificationResponseDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.NotificationEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.MentionEvent;
import project.realtimechatapplication.model.type.NotificationType;
import project.realtimechatapplication.model.type.Reaction;
import project.realtimechatapplication.repository.jpa.ChatRoomRepository;
import project.realtimechatapplication.repository.jpa.MessageRepository;
import project.realtimechatapplication.repository.jpa.NotificationRepository;
import project.realtimechatapplication.repository.jpa.UserRepository;
import project.realtimechatapplication.service.NotificationService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @Transactional
  public void createAndSendNotifications(ChatDto chatDto, MessageSendResponseDto responseDto) {
    ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(chatDto.getRoomCode())
        .orElseThrow(ChatRoomNotFoundException::new);

    List<MemberChatRoomEntity> members = chatRoom.getMemberChatRooms();
    MessageEntity messageEntity = messageRepository.findById(responseDto.getMessageId())
        .orElseThrow(MessageNotExistException::new);

    members.stream()
        .filter(member -> !member.getUser().getUsername().equals(chatDto.getSender()))
        .forEach(member -> {
          NotificationEntity notification = NotificationEntity.notification(
              NotificationType.MESSAGE, member.getUser(), messageEntity);
          notificationRepository.save(notification);
          messagingTemplate.convertAndSend("/topic/notifications/" + member.getUser().getId(),
              MessageNotificationResponseDto.to(notification, chatRoom.getName(), chatDto.getSender(),
                  responseDto.getMessage()));
        });
  }

  @Transactional
  public void createAndSendReactionNotification(Long messageId, String reactorUsername, Reaction reaction) {
    MessageEntity message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotExistException::new);
    UserEntity messageOwner = message.getUser();

    NotificationEntity notification = NotificationEntity.notification(
        NotificationType.REACTION, messageOwner, message);

    notificationRepository.save(notification);
    messagingTemplate.convertAndSend("/topic/notifications/" + messageOwner.getId(),
        ReactionNotificationResponseDto.to(notification, messageOwner.getUsername(), reaction));
  }

  @Transactional
  public void createAndSendAddMemberNotification(String chatRoomName, Long addingMemberId, String inviterUsername) {
    UserEntity memberToBeAdded = userRepository.findById(addingMemberId)
        .orElseThrow(UserNotFoundException::new);

    NotificationEntity notification = NotificationEntity.notification(
        NotificationType.INVITATION, memberToBeAdded);
    notificationRepository.save(notification);
    messagingTemplate.convertAndSend("/topic/notifications/" + addingMemberId,
        AddMemberNotificationResponseDto.to(notification, chatRoomName));
  }

  @EventListener
  @Transactional
  public void handleMentionEvent(MentionEvent mentionEvent) {
    for (String username : mentionEvent.getMentions()) {
      UserEntity user = userRepository.findByUsername(username)
          .orElseThrow(UserNotFoundException::new);

      NotificationEntity notification = NotificationEntity.notification(NotificationType.MENTION, user);

      notificationRepository.save(notification);

      messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/notifications", notification);
    }
  }
}
