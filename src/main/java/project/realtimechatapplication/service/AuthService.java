package project.realtimechatapplication.service;

import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;

public interface AuthService {

  void usernameCheck(UsernameCheckRequestDto dto);
}
