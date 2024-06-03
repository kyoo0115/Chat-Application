package project.realtimechatapplication.service;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.model.type.MessageType;

public interface MessageService {

    MessageSendResponseDto sendMessage(ChatDto chatDto);

    ChatDto makeEnterMessageAndSetSessionAttribute(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor);
}
