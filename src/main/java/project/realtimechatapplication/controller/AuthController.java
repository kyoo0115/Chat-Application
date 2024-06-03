package project.realtimechatapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignInRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.CheckVerificationResponseDto;
import project.realtimechatapplication.dto.response.auth.SendVerificationEmailResponseDto;
import project.realtimechatapplication.dto.response.auth.SignUpResponseDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;
import project.realtimechatapplication.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/username-check")
  public ResponseEntity<?> usernameCheck(
      @RequestBody @Valid UsernameCheckRequestDto dto
  ) {
    authService.usernameCheck(dto);
    return UsernameCheckResponseDto.success();
  }

  @PostMapping("/send-verification-email")
  public ResponseEntity<?> sendVerificationEmail(
      @RequestBody @Valid SendVerificationEmailRequestDto dto
  ) {
    authService.sendVerificationEmail(dto);
    return SendVerificationEmailResponseDto.success();
  }

  @PostMapping("/check-verification-email")
  public ResponseEntity<?> checkVerification(
      @RequestBody @Valid CheckVerificationRequestDto dto
  ) {
    authService.checkVerification(dto);
    return CheckVerificationResponseDto.success();
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(
      @RequestBody @Valid SignUpRequestDto dto
  ) {
    authService.signUp(dto);
    return SignUpResponseDto.success();
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(
      @RequestBody @Valid SignInRequestDto dto
  ) {
    log.info("signIn : {}", dto.getUsername());
    return authService.signIn(dto);
  }
}
