package project.realtimechatapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.request.chat.DeleteMessageRequestDto;
import project.realtimechatapplication.dto.request.chat.EditMessageRequestDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.dto.response.chat.DeleteMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.EditMessageResponseDto;
import project.realtimechatapplication.dto.response.chat.MessageSendResponseDto;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchMessageEntity;
import project.realtimechatapplication.service.MessageService;
import project.realtimechatapplication.service.NotificationService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name = "메세지 컨트롤러", description = "메세지 관리를 위한 API들")
public class MessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate msgOperation;
  private final NotificationService notificationService;

  @MessageMapping("/chat/enter")
  public void enterChatRoom(
      @Payload @Valid ChatDto chatDto,
      SimpMessageHeaderAccessor headerAccessor) {
    log.info("enterChatRoom : {}", chatDto.getSender());
    ChatDto newChatDto = messageService.makeEnterMessageAndSetSessionAttribute(chatDto,
        headerAccessor);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(), newChatDto);
  }

  @MessageMapping("/chat/send")
  public void sendChatMessage(
      @Payload @Valid ChatDto chatDto) {
    log.info("sendChatMessage : {}", chatDto.getSender());
    MessageSendResponseDto messageSendResponseDto = messageService.sendMessage(chatDto);
    msgOperation.convertAndSend("/topic/chat/room/" + chatDto.getRoomCode(),
        messageSendResponseDto);
    notificationService.createAndSendNotifications(chatDto, messageSendResponseDto);
  }

  @Operation
      (
          summary = "해당 메세지 수정",
          description = "사용자는 본인 메세지만 수정할 수 있습니다.<br>"
              + "[messageId]는 사용자는 본인 메세지의 [id]를 지정할 수 있습니다."
      )
  @PutMapping("/chat/{messageId}")
  public ResponseEntity<?> editChatMessage(
      @RequestBody @Valid EditMessageRequestDto dto,
      @AuthenticationPrincipal final User user,
      @PathVariable Long messageId
  ) {
    log.info("editChatMessage : {}", user.getUsername());
    EditMessageResponseDto messageEditResponseDto = messageService.editMessage(dto,
        user.getUsername(), messageId);
    msgOperation.convertAndSend("/topic/chat/room/" + dto.getRoomCode(), messageEditResponseDto);
    return CustomResponse.success(messageEditResponseDto);
  }

  @Operation
      (
          summary = "해당 메세지 삭제",
          description = "사용자는 본인 메세지만 삭제할 수 있습니다.<br>"
              + "[messageId]는 사용자는 본인 메세지의 [id]를 지정할 수 있습니다."
      )
  @DeleteMapping("/chat/{messageId}")
  public ResponseEntity<?> deleteChatMessage(
      @RequestBody @Valid DeleteMessageRequestDto dto,
      @AuthenticationPrincipal final User user,
      @PathVariable Long messageId
  ) {
    log.info("deleteChatMessage : {}", user.getUsername());
    DeleteMessageResponseDto deleteMessageResponseDto = messageService.deleteMessage(dto,
        user.getUsername(), messageId);
    msgOperation.convertAndSend("/topic/chat/room/" + dto.getRoomCode(), deleteMessageResponseDto);
    return CustomResponse.success(deleteMessageResponseDto);
  }

  @Operation(
      summary = "채팅방 내 메세지 검색",
      description = "채팅방 내에서 메세지를 검색할 수 있습니다."
  )
  @GetMapping("/chat/search")
  public ResponseEntity<?> searchMessages(
      @RequestParam Long chatRoomId,
      @RequestParam String query,
      @AuthenticationPrincipal User user
  ) {
    log.info("searchMessages in chatRoomId : {} with query : {}", chatRoomId, query);
    List<ElasticsearchMessageEntity> messages = messageService.searchMessages(chatRoomId, query, user.getUsername());
    return CustomResponse.success(messages);
  }
}
