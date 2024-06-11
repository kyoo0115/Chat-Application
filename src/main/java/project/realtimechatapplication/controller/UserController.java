package project.realtimechatapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.user.EditUserRequestDto;
import project.realtimechatapplication.dto.request.user.UserDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "사용자 컨트롤러", description = "사용자 관리를 위한 API들")
public class UserController {

  private final UserService userService;

  @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
  @GetMapping("/{userId}")
  public ResponseEntity<?> viewUser(
      @PathVariable Long userId
  ) {
    UserDto user = userService.getUserById(userId);
    return CustomResponse.success(user);
  }

  @Operation(summary = "사용자 수정", description = "ID로 사용자를 수정합니다.")
  @PutMapping("/{userId}")
  public ResponseEntity<?> editUser(
      @PathVariable Long userId,
      @RequestBody EditUserRequestDto dto,
      @AuthenticationPrincipal User user) {

    UserDto updatedUser = userService.editUser(userId, dto, user.getUsername());
    return CustomResponse.success(updatedUser);
  }

  @Operation(summary = "사용자 삭제", description = "ID로 사용자를 삭제합니다.")
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(
      @PathVariable Long userId,
      @AuthenticationPrincipal User user) {

    userService.deleteUser(userId, user.getUsername());
    return CustomResponse.success();
  }

  @GetMapping("/chatroom/{roomCode}")
  public ResponseEntity<?> searchChatRoomMembers(
      @PathVariable String roomCode,
      @RequestParam String query) {

    List<UserDto> members = userService.searchChatRoomMembers(roomCode, query);
    return CustomResponse.success(members);
  }
}
