package project.realtimechatapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "인증 컨트롤러", description = "인증 및 회원 관리를 위한 API들")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "유저네임 확인", description = "유저네임 중복 확인을 수행합니다.")
  @PostMapping("/username-check")
  public ResponseEntity<?> usernameCheck(
      @RequestBody @Valid UsernameCheckRequestDto dto
  ) {
    authService.usernameCheck(dto);
    return CustomResponse.success();
  }

  @Operation(summary = "인증 이메일 발송", description = "유저에게 인증 이메일을 발송합니다.")
  @PostMapping("/send-verification-email")
  public ResponseEntity<?> sendVerificationEmail(
      @RequestBody @Valid SendVerificationEmailRequestDto dto
  ) {
    authService.sendVerificationEmail(dto);
    return CustomResponse.success();
  }

  @Operation(summary = "인증 이메일 확인", description = "유저의 인증 이메일을 확인합니다.")
  @PostMapping("/check-verification-email")
  public ResponseEntity<?> checkVerification(
      @RequestBody @Valid CheckVerificationRequestDto dto
  ) {
    authService.checkVerification(dto);
    return CustomResponse.success();
  }

  @Operation(summary = "회원 가입", description = "새 유저를 등록합니다.")
  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(
      @RequestBody @Valid SignUpRequestDto dto
  ) {
    authService.signUp(dto);
    return CustomResponse.success();
  }

  @Operation(summary = "로그인", description = "기존 유저의 로그인 처리를 합니다.")
  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(
      @RequestBody @Valid SignInRequestDto dto
  ) {
    log.info("signIn : {}", dto.getUsername());
    SignInResponseDto signInResponse = authService.signIn(dto);
    return CustomResponse.success(signInResponse);
  }
}
