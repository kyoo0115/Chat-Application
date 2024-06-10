package project.realtimechatapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.request.chat.DeleteMessageRequestDto;
import project.realtimechatapplication.dto.request.chat.EditMessageRequestDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.dto.response.chat.DeleteMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.EditMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.service.MessageService;
import project.realtimechatapplication.service.NotificationService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class MessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate msgOperation;
  private final NotificationService notificationService;

  @MessageMapping("/chat/enter")
  public void enterChatRoom(
      @Payload @Valid ChatDto chatDto,
      SimpMessageHeaderAccessor headerAccessor) {
    log.info("enterChatRoom : {}", chatDto.getSender());
    ChatDto newChatDto = messageService.makeEnterMessageAndSetSessionAttribute(chatDto, headerAccessor);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(), newChatDto);
  }

  @MessageMapping("/chat/send")
  public void sendChatMessage(
      @Payload @Valid ChatDto chatDto) {
    log.info("sendChatMessage : {}", chatDto.getSender());
    MessageSendResponseDto messageSendResponseDto = messageService.sendMessage(chatDto);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(), messageSendResponseDto);
    notificationService.createAndSendNotifications(chatDto, messageSendResponseDto);
  }

  @PutMapping("/chat/{messageId}")
  public ResponseEntity<?> editChatMessage(
      @RequestBody @Valid EditMessageRequestDto dto,
      @AuthenticationPrincipal final User user,
               @PathVariable Long messageId
  ) {
    log.info("editChatMessage : {}", dto.getSender());
    EditMessageResponseDto messageEditResponseDto = messageService.editMessage(dto, user.getUsername(), messageId);
    msgOperation.convertAndSend("/topic/chat/room/" + dto.getRoomCode(), messageEditResponseDto);
    return CustomResponse.success(messageEditResponseDto);
  }

  @DeleteMapping("/chat/{messageId}")
  public ResponseEntity<?> deleteChatMessage(
      @RequestBody @Valid DeleteMessageRequestDto dto,
      @AuthenticationPrincipal final User user,
               @PathVariable Long messageId
  ) {
    log.info("deleteChatMessage : {}", dto.getSender());
    DeleteMessageResponseDto deleteMessageResponseDto = messageService.deleteMessage(dto, user.getUsername(), messageId);
    msgOperation.convertAndSend("/topic/chat/room/" + dto.getRoomCode(), deleteMessageResponseDto);
    return CustomResponse.success(deleteMessageResponseDto);
  }
}
