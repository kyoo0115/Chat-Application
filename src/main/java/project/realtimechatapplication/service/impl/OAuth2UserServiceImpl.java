package project.realtimechatapplication.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.realtimechatapplication.dto.request.auth.KakaoLoginRequestDto;
import project.realtimechatapplication.dto.response.auth.KakaoLoginResponseDto;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.provider.TokenProvider;
import project.realtimechatapplication.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final RestTemplate restTemplate;
  private final TokenProvider tokenProvider;

  private final String REST_API_KEY = "9b0ff9395d3346c83412725ba424f35d";
  private final String REDIRECT_URI = "http://localhost:8080/oauth2/callback/kakao";
  private final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
  private final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

  public KakaoLoginResponseDto kakaoLogin(KakaoLoginRequestDto requestDto) {

    Map<String, String> tokenRequest = new HashMap<>();
    tokenRequest.put("grant_type", "authorization_code");
    tokenRequest.put("client_id", REST_API_KEY);
    tokenRequest.put("redirect_uri", REDIRECT_URI);
    tokenRequest.put("code", requestDto.getAuthorizationCode());

    ResponseEntity<Map> tokenResponseEntity = restTemplate.postForEntity(TOKEN_URI, tokenRequest, Map.class);

    if (!tokenResponseEntity.getStatusCode().is2xxSuccessful() || tokenResponseEntity.getBody() == null) {
      throw new IllegalArgumentException("Failed to obtain access token from Kakao");
    }

    Map<String, Object> tokenResponse = tokenResponseEntity.getBody();
    String accessToken = (String) tokenResponse.get("access_token");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<Map> userInfoResponseEntity = restTemplate.exchange(USER_INFO_URI, HttpMethod.GET, entity, Map.class);

    if (!userInfoResponseEntity.getStatusCode().is2xxSuccessful() || userInfoResponseEntity.getBody() == null) {
      throw new IllegalArgumentException("Failed to retrieve user info from Kakao");
    }

    Map<String, Object> userInfo = userInfoResponseEntity.getBody();
    String id = userInfo.get("id").toString();
    Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
    String nickname = properties.get("nickname").toString();
    String email = properties.get("email").toString();

    Optional<UserEntity> existingUser = userRepository.findByUsername("kakao_" + id);
    UserEntity user;
    if (existingUser.isPresent()) {
      user = existingUser.get();
      user.setName(nickname);
    } else {
      user = UserEntity.builder()
          .email(email)
          .username("kakao_" + id)
          .name(nickname)
          .role("ROLE_USER")
          .build();
      userRepository.save(user);
    }

    String token = tokenProvider.createToken(user.getUsername());

    return new KakaoLoginResponseDto(token, user.getUsername(), user.getEmail());
  }
}