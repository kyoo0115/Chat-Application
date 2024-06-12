package project.realtimechatapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "oauth 로그인 컨트롤러", description = "카카오 로그인 컨트롤러")
public class OAuthController {

  private final OAuth2UserServiceImpl oAuth2UserService;

  @Operation(summary = "카카오 로그인", description = "카카오 로그인을 수행합니다.")
  @PostMapping("/kakao")
  public KakaoLoginResponseDto kakaoAuth(@RequestBody KakaoLoginRequestDto requestDto) {
    return oAuth2UserService.kakaoLogin(requestDto);
  }
}
