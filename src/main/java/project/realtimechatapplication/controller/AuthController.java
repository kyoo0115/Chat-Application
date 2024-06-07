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
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.dto.response.auth.SignInResponseDto;
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
    return CustomResponse.success();
  }

  @PostMapping("/send-verification-email")
  public ResponseEntity<?> sendVerificationEmail(
      @RequestBody @Valid SendVerificationEmailRequestDto dto
  ) {
    authService.sendVerificationEmail(dto);
    return CustomResponse.success();
  }

  @PostMapping("/check-verification-email")
  public ResponseEntity<?> checkVerification(
      @RequestBody @Valid CheckVerificationRequestDto dto
  ) {
    authService.checkVerification(dto);
    return CustomResponse.success();
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(
      @RequestBody @Valid SignUpRequestDto dto
  ) {
    authService.signUp(dto);
    return CustomResponse.success();
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(
      @RequestBody @Valid SignInRequestDto dto
  ) {
    log.info("signIn : {}", dto.getUsername());
    SignInResponseDto signInResponse = authService.signIn(dto);
    return CustomResponse.success(signInResponse);
  }
}
