package project.realtimechatapplication.controller;

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
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.user.EditUserRequestDto;
import project.realtimechatapplication.dto.request.user.UserDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.service.AuthService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final AuthService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<?> viewUser(
      @PathVariable Long userId
  ) {
    UserDto user = userService.getUserById(userId);
    return CustomResponse.success(user);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> editUser(
      @PathVariable Long userId,
      @RequestBody EditUserRequestDto dto,
      @AuthenticationPrincipal User user) {

    UserDto updatedUser = userService.editUser(userId, dto, user.getUsername());
    return CustomResponse.success(updatedUser);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(
      @PathVariable Long userId,
      @AuthenticationPrincipal User user) {

    userService.deleteUser(userId, user.getUsername());
    return CustomResponse.success();
  }
}
