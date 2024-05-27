package project.realtimechatapplication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.VerificationEntity;
import project.realtimechatapplication.exception.impl.EmailAlreadyExistsException;
import project.realtimechatapplication.exception.impl.EmailNotMatchedException;
import project.realtimechatapplication.exception.impl.EmailSendErrorException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.exception.impl.UsernameAlreadyExistsException;
import project.realtimechatapplication.exception.impl.VerificationNumberNotMatchedException;
import project.realtimechatapplication.provider.EmailProvider;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.repository.VerificationRepository;
import project.realtimechatapplication.model.VerificationNumberGenerator;
import project.realtimechatapplication.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;

  private final EmailProvider emailProvider;

  private final PasswordEncoder passwordEncoder;

  @Override
  public void usernameCheck(UsernameCheckRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
  }

  @Override
  public void sendVerificationEmail(SendVerificationEmailRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
    validateEmailNotExists(dto.getEmail());

    String verificationNumber = VerificationNumberGenerator.generateVerificationNumber();
    boolean sendMail = emailProvider.sendVerificationEmail(dto.getEmail(), (verificationNumber));

    if (!sendMail) throw new EmailSendErrorException();

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

    if(!email.equals(verificationEntity.getEmail())) {
      throw new EmailNotMatchedException();
    }

    if(!verificationNumber.equals(verificationEntity.getVerificationNumber())) {
      throw new VerificationNumberNotMatchedException();
    }
  }
}
