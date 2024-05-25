package project.realtimechatapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;
import project.realtimechatapplication.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  @Override
  public void usernameCheck(UsernameCheckRequestDto dto) {

    userRepository.findByUsername(dto.getUsername())
        .ifPresent(user -> {
          throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
        });
  }
}
