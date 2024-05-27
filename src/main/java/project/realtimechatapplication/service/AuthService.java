package project.realtimechatapplication.service;

import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;

public interface AuthService {

  void usernameCheck(UsernameCheckRequestDto dto);
  void sendVerificationEmail(SendVerificationEmailRequestDto dto);
  void checkVerification(CheckVerificationRequestDto dto);
  void signUp(SignUpRequestDto dto);
}
