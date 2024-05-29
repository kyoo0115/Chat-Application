package project.realtimechatapplication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.realtimechatapplication.model.CustomOAuth2User;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);
    String oauthClientName = userRequest.getClientRegistration().getClientName();

    try{
      System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    UserEntity user;
    if (oauthClientName.equals("kakao")) {
      user = processKakaoUser(oAuth2User);
    } else if (oauthClientName.equals("google")) {
      user = processGoogleUser(oAuth2User);
    } else {
      throw new OAuth2AuthenticationException("Unsupported client: " + oauthClientName);
    }

    userRepository.save(user);
    return new CustomOAuth2User(user.getUsername());
  }

  private UserEntity processKakaoUser(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();
    String id = attributes.get("id").toString();
    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
    String nickname = properties.get("nickname").toString();

    String username = "kakao_" + id;
    String email = username + "@kakao.com";

    Optional<UserEntity> existingUser = userRepository.findByUsername(username);
    UserEntity user;

    if (existingUser.isPresent()) {
      user = existingUser.get();
      user.setName(nickname);
    } else {
      user = UserEntity.builder()
          .email(email)
          .username(username)
          .password("password")
          .name(nickname)
          .role("ROLE_USER")
          .build();
    }
    return user;
  }

  private UserEntity processGoogleUser(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();
    String id = attributes.get("sub").toString();
    String email = attributes.get("email").toString();
    String name = attributes.get("name").toString();

    String username = "google_" + id;

    Optional<UserEntity> existingUser = userRepository.findByUsername(username);
    UserEntity user;

    if (existingUser.isPresent()) {
      user = existingUser.get();
      user.setEmail(email);
      user.setName(name);
    } else {
      user = UserEntity.builder()
          .email(email)
          .username(username)
          .password("password")
          .name(name)
          .role("ROLE_USER")
          .build();
    }
    return user;
  }
}