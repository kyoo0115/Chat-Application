package project.realtimechatapplication.service;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;

public interface AuthService {

  void usernameCheck(UsernameCheckRequestDto dto);
  void sendVerificationEmail(SendVerificationEmailRequestDto dto);

  void checkVerification(CheckVerificationRequestDto dto);
}
