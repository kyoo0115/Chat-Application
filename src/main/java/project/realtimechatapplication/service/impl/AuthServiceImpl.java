package project.realtimechatapplication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignInRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.SignInResponseDto;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.VerificationEntity;
import project.realtimechatapplication.exception.impl.EmailAlreadyExistsException;
import project.realtimechatapplication.exception.impl.EmailNotMatchedException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.exception.impl.UsernameAlreadyExistsException;
import project.realtimechatapplication.exception.impl.VerificationNumberNotMatchedException;
import project.realtimechatapplication.exception.impl.WrongPasswordException;
import project.realtimechatapplication.model.VerificationNumberGenerator;
import project.realtimechatapplication.provider.EmailProvider;
import project.realtimechatapplication.provider.TokenProvider;
import project.realtimechatapplication.repository.jpa.UserRepository;
import project.realtimechatapplication.repository.jpa.VerificationRepository;
import project.realtimechatapplication.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;

  private final EmailProvider emailProvider;

  private final PasswordEncoder passwordEncoder;

  private final TokenProvider tokenProvider;

  @Override
  public void usernameCheck(UsernameCheckRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
  }

  @Override
  @Transactional
  public void sendVerificationEmail(SendVerificationEmailRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
    validateEmailNotExists(dto.getEmail());

    String verificationNumber = VerificationNumberGenerator.generateVerificationNumber();
    emailProvider.sendVerificationEmail(dto.getEmail(), (verificationNumber));

    VerificationEntity verificationEntity = VerificationEntity.of(dto, verificationNumber);
    verificationRepository.save(verificationEntity);
  }

  @Override
  public void checkVerification(CheckVerificationRequestDto dto) {
    validateVerification(dto.getUsername(), dto.getEmail(), dto.getVerificationNumber());
  }

  @Override
  @Transactional
  public void signUp(SignUpRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
    validateEmailNotExists(dto.getEmail());
    validateVerification(dto.getUsername(), dto.getEmail(), dto.getVerificationNumber());

    String password = dto.getPassword();
    String encodedPassword = passwordEncoder.encode(password);
    dto.setPassword(encodedPassword);

    UserEntity userEntity = UserEntity.of(dto);

    verificationRepository.deleteByUsername(userEntity.getUsername());
    userRepository.save(userEntity);
  }

  @Override
  public SignInResponseDto signIn(SignInRequestDto dto) {

    UserEntity user = userRepository.findByUsername(dto.getUsername())
        .orElseThrow(UserNotFoundException::new);

    String password = dto.getPassword();
    String encodedPassword = user.getPassword();

    if (!passwordEncoder.matches(password, encodedPassword)) {
      throw new WrongPasswordException();
    }

    String token = tokenProvider.createToken(user.getUsername());

    return new SignInResponseDto(token, user.getUsername());
  }

  private void validateUsernameNotExists(String username) {
    userRepository.findByUsername(username)
        .ifPresent(user -> {
          throw new UsernameAlreadyExistsException();
        });
  }

  private void validateEmailNotExists(String email) {
    userRepository.findByEmail(email)
        .ifPresent(user -> {
          throw new EmailAlreadyExistsException();
        });
  }

  private void validateVerification(String username, String email, String verificationNumber) {
    VerificationEntity verificationEntity = verificationRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    if (!email.equals(verificationEntity.getEmail())) {
      throw new EmailNotMatchedException();
    }

    if (!verificationNumber.equals(verificationEntity.getVerificationNumber())) {
      throw new VerificationNumberNotMatchedException();
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));

    return User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}
