package project.realtimechatapplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.model.type.MessageType;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.MessageRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.MessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageSendResponseDto sendMessage(ChatDto chatDto) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(chatDto.getRoomCode())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

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
    public ChatDto makeEnterMessageAndSetSessionAttribute(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatDto.getSender());
        return chatDto;
    }
}
