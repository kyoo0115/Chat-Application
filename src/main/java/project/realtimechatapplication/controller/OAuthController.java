package project.realtimechatapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.service.impl.OAuth2UserServiceImpl;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class OAuthController {

  private final OAuth2UserServiceImpl oAuth2UserService;

  @PostMapping("/google")
  public String googleSignIn() {
    return "google";
  }

//  @PostMapping("/oauth2/kakao")
//  public String kakaoSignIn(@RequestBody @Valid OAuthRequestDto requestDto) {
//    return oAuth2UserService.processOAuthSignIn("kakao", requestDto);
//  }

  @PostMapping("/callback/naver")
  public String naverSignIn() {
    return "naver";
  }
}
