package project.realtimechatapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;
import project.realtimechatapplication.service.AuthService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/username-check")
  public ResponseEntity<? super UsernameCheckResponseDto> usernameCheck(
      @RequestBody @Valid UsernameCheckRequestDto dto
  ) {
    authService.usernameCheck(dto);
    return UsernameCheckResponseDto.success();
  }
}
