package project.realtimechatapplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
@CrossOrigin
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/create")
  public ResponseEntity<?> createChatRoom(
      @RequestBody @Valid final ChatRoomCreateRequestDto dto,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.createChatRoom(dto, user.getUsername());

    return ResponseEntity.status(HttpStatus.OK).body(chatRoom);
  }

  @PostMapping("/addMember")
  public ResponseEntity<?> addMemberToChatRoom(
      @RequestBody @Valid final ChatRoomAddRequestDto dto,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.addMemberToChatRoom(dto);
    return ResponseEntity.status(HttpStatus.OK).body(chatRoom);
  }

  @GetMapping("/room")
  public ResponseEntity<List<ChatRoomDto>> showChatRoomList(
      @AuthenticationPrincipal final User user) {
    List<ChatRoomDto> chatRooms = chatRoomService.searchChatRoomList(user.getUsername());
    return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
  }
}
