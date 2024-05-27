package project.realtimechatapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.VerificationEntity;
import project.realtimechatapplication.provider.EmailProvider;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.repository.VerificationRepository;
import project.realtimechatapplication.model.VerificationNumberGenerator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;

  private final EmailProvider emailProvider;

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

    if (!sendMail) {
      throw new RuntimeException("Email send failure.");
    }

    VerificationEntity verificationEntity = VerificationEntity.of(dto, verificationNumber);
    verificationRepository.save(verificationEntity);
  }

  @Override
  public void checkVerification(CheckVerificationRequestDto dto) {
    validateVerification(dto.getUsername(), dto.getEmail(), dto.getVerificationNumber());
  }

  @Override
  public void signUp(SignUpRequestDto dto) {

    validateUsernameNotExists(dto.getUsername());
    validateEmailNotExists(dto.getEmail());
    validateVerification(dto.getUsername(), dto.getEmail(), dto.getVerificationNumber());

    UserEntity userEntity = UserEntity.of(dto);
    userRepository.save(userEntity);
  }

  private void validateUsernameNotExists(String username) {
    userRepository.findByUsername(username)
        .ifPresent(user -> {
          throw new RuntimeException("User already exists.");
        });
  }

  private void validateEmailNotExists(String email) {
    userRepository.findByEmail(email)
        .ifPresent(user -> {
          throw new RuntimeException("Email already exists.");
        });
  }

  private void validateVerification(String username, String email, String verificationNumber) {
    VerificationEntity verificationEntity = verificationRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found."));

    if(!email.equals(verificationEntity.getEmail())) {
      throw new RuntimeException("Email is not matched.");
    }

    if(!verificationNumber.equals(verificationEntity.getVerificationNumber())) {
      throw new RuntimeException("Verification number is not matched.");
    }
  }
}
