package project.realtimechatapplication.service;

import java.util.List;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.request.chat.DeleteMessageRequestDto;
import project.realtimechatapplication.dto.request.chat.EditMessageRequestDto;
import project.realtimechatapplication.dto.response.chat.DeleteMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.EditMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchMessageEntity;

public interface MessageService {

  MessageSendResponseDto sendMessage(ChatDto dto);

  ChatDto makeEnterMessageAndSetSessionAttribute(ChatDto dto,
      SimpMessageHeaderAccessor headerAccessor);

  EditMessageResponseDto editMessage(EditMessageRequestDto dto, String username, Long messageId);

  DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto dto, String username,
      Long messageId);

  List<ElasticsearchMessageEntity> searchMessages(Long chatRoomId, String query, String username);
}
