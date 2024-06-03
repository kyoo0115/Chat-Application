package project.realtimechatapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.service.MessageService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class MessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate msgOperation;

  @MessageMapping("/chat/enter")
  public void enterChatRoom(
      @Payload ChatDto chatDto,
      SimpMessageHeaderAccessor headerAccessor) {
    log.info("enterChatRoom : {}", chatDto.getSender());
    ChatDto newChatDto = messageService.makeEnterMessageAndSetSessionAttribute(chatDto, headerAccessor);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(), newChatDto);
  }

  @MessageMapping("/chat/send")
  public void sendChatMessage(
      @Payload ChatDto chatDto) {
    log.info("sendChatMessage : {}", chatDto.getSender());
    MessageSendResponseDto messageSendResponseDto = messageService.sendMessage(chatDto);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(), messageSendResponseDto);
  }
}
