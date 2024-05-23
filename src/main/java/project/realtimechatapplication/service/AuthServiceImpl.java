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
  public ResponseEntity<? super UsernameCheckResponseDto> usernameCheck(UsernameCheckRequestDto dto) {
    try {
      String username = dto.getUsername();
      boolean isExist = userRepository.existsByUsername(username);
      if(isExist) return UsernameCheckResponseDto.duplicateUsername();

    } catch (Exception e) {
      return UsernameCheckResponseDto.databaseError();
    }
    return UsernameCheckResponseDto.success();
  }
}
