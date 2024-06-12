package project.realtimechatapplication.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.NotificationEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.MentionEvent;
import project.realtimechatapplication.model.type.Reaction;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.MessageRepository;
import project.realtimechatapplication.repository.NotificationRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.impl.NotificationServiceImpl;

public class NotificationServiceImplTest {

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SimpMessagingTemplate messagingTemplate;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateAndSendNotifications_Success() {
    ChatDto chatDto = new ChatDto();
    chatDto.setRoomCode("ROOM123");
    chatDto.setSender("senderUser");

    MessageSendResponseDto responseDto = new MessageSendResponseDto();
    responseDto.setMessageId(1L);

    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setName("Test Room");
    MemberChatRoomEntity memberChatRoom = new MemberChatRoomEntity();
    UserEntity user = new UserEntity();
    user.setUsername("receiverUser");
    memberChatRoom.setUser(user);
    chatRoom.setMemberChatRooms(Collections.singletonList(memberChatRoom));

    MessageEntity messageEntity = new MessageEntity();

    when(chatRoomRepository.findByRoomCode(anyString())).thenReturn(Optional.of(chatRoom));
    when(messageRepository.findById(anyLong())).thenReturn(Optional.of(messageEntity));

    notificationService.createAndSendNotifications(chatDto, responseDto);

    verify(notificationRepository).save(any(NotificationEntity.class));

  }

  @Test
  public void testCreateAndSendNotifications_ChatRoomNotFound() {
    ChatDto chatDto = new ChatDto();
    chatDto.setRoomCode("ROOM123");
    chatDto.setSender("senderUser");

    MessageSendResponseDto responseDto = new MessageSendResponseDto();
    responseDto.setMessageId(1L);

    when(chatRoomRepository.findByRoomCode(anyString())).thenReturn(Optional.empty());

    assertThrows(ChatRoomNotFoundException.class,
        () -> notificationService.createAndSendNotifications(chatDto, responseDto));
  }

  @Test
  public void testCreateAndSendReactionNotification_Success() {
    MessageEntity message = new MessageEntity();
    UserEntity messageOwner = new UserEntity();
    messageOwner.setId(1L);
    message.setUser(messageOwner);

    when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));

    notificationService.createAndSendReactionNotification(1L, "reactorUser", Reaction.HEART);

    verify(notificationRepository).save(any(NotificationEntity.class));
  }

  @Test
  public void testCreateAndSendReactionNotification_MessageNotExist() {
    when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(MessageNotExistException.class,
        () -> notificationService.createAndSendReactionNotification(1L, "reactorUser",
            Reaction.HEART));
  }

  @Test
  public void testCreateAndSendAddMemberNotification_Success() {
    UserEntity memberToBeAdded = new UserEntity();
    memberToBeAdded.setId(1L);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(memberToBeAdded));

    notificationService.createAndSendAddMemberNotification("Test Room", 1L, "inviterUser");

    verify(notificationRepository).save(any(NotificationEntity.class));
  }

  @Test
  public void testCreateAndSendAddMemberNotification_UserNotFound() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> notificationService.createAndSendAddMemberNotification("Test Room", 1L,
            "inviterUser"));
  }

  @Test
  public void testHandleMentionEvent_Success() {
    MentionEvent mentionEvent = new MentionEvent();
    mentionEvent.setMentions(Arrays.asList("user1", "user2"));

    UserEntity user1 = new UserEntity();
    user1.setUsername("user1");
    UserEntity user2 = new UserEntity();
    user2.setUsername("user2");

    when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
    when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));

    notificationService.handleMentionEvent(mentionEvent);

    verify(notificationRepository, times(2)).save(any(NotificationEntity.class));
    verify(messagingTemplate, times(2)).convertAndSendToUser(anyString(), anyString(), any());
  }

  @Test
  public void testHandleMentionEvent_UserNotFound() {
    MentionEvent mentionEvent = new MentionEvent();
    mentionEvent.setMentions(Collections.singletonList("user1"));

    when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> notificationService.handleMentionEvent(mentionEvent));
  }
}
