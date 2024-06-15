package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.request.chat.DeleteMessageRequestDto;
import project.realtimechatapplication.dto.request.chat.EditMessageRequestDto;
import project.realtimechatapplication.dto.response.chat.DeleteMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.EditMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchMessageEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.UnauthorizedMessageDeletionException;
import project.realtimechatapplication.exception.impl.UnauthorizedMessageEditException;
import project.realtimechatapplication.exception.impl.UnauthorizedRoomOwnerException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.MentionEvent;
import project.realtimechatapplication.model.type.MessageStatus;
import project.realtimechatapplication.repository.jpa.ChatRoomRepository;
import project.realtimechatapplication.repository.jpa.MessageRepository;
import project.realtimechatapplication.repository.jpa.UserRepository;
import project.realtimechatapplication.repository.elasticsearch.ElasticsearchMessageRepository;
import project.realtimechatapplication.service.MessageService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final ElasticsearchMessageRepository elasticsearchMessageRepository;

  private final ElasticsearchOperations elasticsearchOperations;
  private final ApplicationEventPublisher eventPublisher;

  private static final String INDEX_NAME = "chat_messages";

  @Override
  @Transactional
  public MessageSendResponseDto sendMessage(ChatDto chatDto) {
    ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(chatDto.getRoomCode())
        .orElseThrow(ChatRoomNotFoundException::new);

    UserEntity sender = userRepository.findByUsername(chatDto.getSender())
        .orElseThrow(UserNotFoundException::new);

    MessageEntity messageEntity = MessageEntity.of(chatDto.getType(), chatDto.getMessage(),
        chatRoom, sender);

    messageRepository.save(messageEntity);

    ElasticsearchMessageEntity elasticsearchMessageEntity = ElasticsearchMessageEntity.from(messageEntity);

    IndexQuery indexQuery = new IndexQueryBuilder()
        .withId(elasticsearchMessageEntity.getId().toString())
        .withObject(elasticsearchMessageEntity)
        .build();

    elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));

    List<String> mentions = extractMentions(chatDto.getMessage());

    MessageSendResponseDto responseDto = MessageSendResponseDto.builder()
        .messageId(messageEntity.getId())
        .message(messageEntity.getMessage())
        .chatRoomId(chatRoom.getId())
        .timestamp(messageEntity.getCreatedAt())
        .build();

    if (!mentions.isEmpty()) {
      MentionEvent mentionEvent = new MentionEvent(chatDto.getRoomCode(), chatDto.getMessage(),
          chatDto.getSender(), mentions);
      eventPublisher.publishEvent(mentionEvent);
    }

    return responseDto;
  }

  @Override
  public ChatDto makeEnterMessageAndSetSessionAttribute(ChatDto dto,
      SimpMessageHeaderAccessor headerAccessor) {
    headerAccessor.getSessionAttributes().put("username", dto.getSender());
    return dto;
  }

  @Override
  @Transactional
  public EditMessageResponseDto editMessage(EditMessageRequestDto dto, String username,
      Long messageId) {

    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    MessageEntity message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotExistException::new);

    if (!message.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedMessageEditException();
    }

    message.setMessage(dto.getMessage());
    messageRepository.save(message);

    return EditMessageResponseDto.builder()
        .messageId(message.getId())
        .newMessage(message.getMessage())
        .chatRoomId(message.getChatRoom().getId())
        .status(MessageStatus.EDIT)
        .timestamp(message.getModifiedAt())
        .build();
  }

  @Override
  @Transactional
  public DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto dto, String username,
      Long messageId) {

    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    MessageEntity message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotExistException::new);

    if (!message.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedMessageDeletionException();
    }

    messageRepository.delete(message);

    return DeleteMessageResponseDto.builder()
        .id(message.getId())
        .roomCode(dto.getRoomCode())
        .status(MessageStatus.DELETE)
        .build();
  }

  @Transactional(readOnly = true)
  public List<ElasticsearchMessageEntity> searchMessages(Long chatRoomId, String query, String username) {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(ChatRoomNotFoundException::new);

    if (!chatRoom.getOwner().equals(user.getUsername())) {
      throw new UnauthorizedRoomOwnerException();
    }

    return elasticsearchMessageRepository.findByChatRoomIdAndMessageContaining(chatRoomId, query);
  }

  private List<String> extractMentions(String message) {
    Pattern pattern = Pattern.compile("@\\w+");
    Matcher matcher = pattern.matcher(message);
    return matcher.results()
        .map(mr -> mr.group().substring(1)) // Remove '@'
        .collect(Collectors.toList());
  }
}
