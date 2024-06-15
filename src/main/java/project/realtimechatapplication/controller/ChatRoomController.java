package project.realtimechatapplication.controller;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchChatRoomEntity;
import project.realtimechatapplication.service.ChatRoomService;
import project.realtimechatapplication.service.NotificationService;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name = "채팅방 컨트롤러", description = "채팅방 관리를 위한 API들")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final NotificationService notificationService;

  @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
  @PostMapping("/create")
  public ResponseEntity<?> createChatRoom(
      @RequestBody final ChatRoomCreateRequestDto dto,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.createChatRoom(dto, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @Operation(summary = "채팅방 삭제", description = "ID로 기존 채팅방을 삭제합니다.")
  @DeleteMapping("/{roomId}")
  public ResponseEntity<?> deleteChatRoom(
      @PathVariable final Long roomId,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.deleteChatRoom(roomId, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @Operation(summary = "채팅방에 멤버 추가", description = "ID로 기존 채팅방에 멤버를 추가합니다.")
  @PostMapping("/{memberId}")
  public ResponseEntity<?> addMemberToChatRoom(
      @RequestBody final ChatRoomAddRequestDto dto,
      @AuthenticationPrincipal final User user,
      @PathVariable final Long memberId) {
    ChatRoomDto chatRoom = chatRoomService.addMemberToChatRoom(dto, user.getUsername(), memberId);
    notificationService.createAndSendAddMemberNotification(dto.getRoomCode(), memberId, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @Operation(summary = "채팅방에서 멤버 제거", description = "ID로 기존 채팅방에서 멤버를 제거합니다.")
  @DeleteMapping("/{roomId}/members/{memberId}")
  public ResponseEntity<?> removeMemberFromChatRoom(
      @PathVariable final Long memberId,
      @PathVariable final Long roomId,
      @AuthenticationPrincipal final User user) {
    ChatRoomDto chatRoom = chatRoomService.removeMemberFromChatRoom(memberId, roomId, user.getUsername());
    return CustomResponse.success(chatRoom);
  }

  @Operation(summary = "채팅방 목록 조회", description = "인증된 사용자의 채팅방 목록을 조회합니다.")
  @GetMapping("/room")
  public ResponseEntity<?> showChatRoomList(
      @AuthenticationPrincipal final User user) {
    List<ChatRoomDto> chatRooms = chatRoomService.searchChatRoomList(user.getUsername());
    return CustomResponse.success(chatRooms);
  }

  @Operation(
      summary = "채팅방 검색",
      description = "채팅방을 이름으로 검색할 수 있습니다."
  )
  @GetMapping("/search")
  public ResponseEntity<?> searchChatRooms(
      @RequestParam String query
  ) {
    log.info("searchChatRooms with query : {}", query);
    List<ElasticsearchChatRoomEntity> chatRooms = chatRoomService.searchChatRooms(query);
    return CustomResponse.success(chatRooms);
  }
}
