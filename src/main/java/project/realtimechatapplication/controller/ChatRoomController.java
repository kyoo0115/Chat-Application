package project.realtimechatapplication.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
@CrossOrigin
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/create")
  public ResponseEntity<?> createChatRoom(
      @RequestBody final ChatRoomCreateRequestDto dto,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.createChatRoom(dto, user.getUsername());

    return CustomResponse.success(chatRoom);
  }

  @DeleteMapping("/{roomId}")
  public ResponseEntity<?> deleteChatRoom(
      @PathVariable final Long roomId,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.deleteChatRoom(roomId, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @PostMapping("/{memberId}")
  public ResponseEntity<?> addMemberToChatRoom(
               @RequestBody final ChatRoomAddRequestDto dto,
      @AuthenticationPrincipal final User user,
               @PathVariable final Long memberId) {
    ChatRoomDto chatRoom = chatRoomService.addMemberToChatRoom(dto, user.getUsername(), memberId);
    return CustomResponse.success(chatRoom);
  }

  @DeleteMapping("/{roomId}/members/{memberId}")
  public ResponseEntity<?> removeMemberFromChatRoom(
               @PathVariable final Long memberId,
               @PathVariable final Long roomId,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.removeMemberFromChatRoom(memberId, roomId, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @GetMapping("/room")
  public ResponseEntity<?> showChatRoomList(
      @AuthenticationPrincipal final User user) {
    List<ChatRoomDto> chatRooms = chatRoomService.searchChatRoomList(user.getUsername());
    return CustomResponse.success(chatRooms);
  }
}
