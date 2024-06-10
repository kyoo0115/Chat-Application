package project.realtimechatapplication.service;

import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignInRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.request.user.EditUserRequestDto;
import project.realtimechatapplication.dto.request.user.UserDto;
import project.realtimechatapplication.dto.response.auth.SignInResponseDto;

public interface AuthService {

  void usernameCheck(UsernameCheckRequestDto dto);
  void sendVerificationEmail(SendVerificationEmailRequestDto dto);
  void checkVerification(CheckVerificationRequestDto dto);
  void signUp(SignUpRequestDto dto);
  SignInResponseDto signIn(SignInRequestDto dto);
  UserDto getUserById(Long userId);
  UserDto editUser(Long userId, EditUserRequestDto dto, String username);
  void deleteUser(Long userId, String username);
}
