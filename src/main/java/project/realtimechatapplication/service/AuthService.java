package project.realtimechatapplication.service;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignInRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.SignInResponseDto;

public interface AuthService {

  void usernameCheck(UsernameCheckRequestDto dto);
  void sendVerificationEmail(SendVerificationEmailRequestDto dto);
  void checkVerification(CheckVerificationRequestDto dto);
  void signUp(SignUpRequestDto dto);
  ResponseEntity<SignInResponseDto> signIn(SignInRequestDto dto);
}
