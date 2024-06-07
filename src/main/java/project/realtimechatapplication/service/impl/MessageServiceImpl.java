package project.realtimechatapplication.service.impl;

import lombok.RequiredArgsConstructor;
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
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.UnauthorizedMessageDeletionException;
import project.realtimechatapplication.exception.impl.UnauthorizedMessageEditException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.type.MessageStatus;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.MessageRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.MessageService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageSendResponseDto sendMessage(ChatDto chatDto) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(chatDto.getRoomCode())
                .orElseThrow(ChatRoomNotFoundException::new);

        UserEntity sender = userRepository.findByUsername(chatDto.getSender())
                .orElseThrow(UserNotFoundException::new);

        MessageEntity messageEntity = MessageEntity.of(chatDto.getType(), chatDto.getMessage(), chatRoom, sender);

        messageRepository.save(messageEntity);

        return MessageSendResponseDto.builder()
                .messageId(messageEntity.getId())
                .message(messageEntity.getMessage())
                .chatRoomId(chatRoom.getId())
                .timestamp(messageEntity.getCreatedAt())
                .build();
    }

    @Override
    public ChatDto makeEnterMessageAndSetSessionAttribute(ChatDto dto, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", dto.getSender());
        return dto;
    }

    @Override
    @Transactional
    public EditMessageResponseDto editMessage(EditMessageRequestDto dto, String username, Long messageId) {

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
    public DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto dto, String username, Long messageId) {

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
}
