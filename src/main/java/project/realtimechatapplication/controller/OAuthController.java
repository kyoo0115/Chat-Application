package project.realtimechatapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.auth.KakaoLoginRequestDto;
import project.realtimechatapplication.dto.response.auth.KakaoLoginResponseDto;
import project.realtimechatapplication.service.impl.OAuth2UserServiceImpl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {

  private final OAuth2UserServiceImpl oAuth2UserService;

  @PostMapping("/kakao")
  public KakaoLoginResponseDto kakaoAuth(@RequestBody KakaoLoginRequestDto requestDto) {
    return oAuth2UserService.kakaoLogin(requestDto);
  }
}
