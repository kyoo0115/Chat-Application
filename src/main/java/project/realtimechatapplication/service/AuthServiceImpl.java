package project.realtimechatapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.realtimechatapplication.dto.request.auth.CheckVerificationRequestDto;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
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

    userRepository.findByUsername(dto.getUsername())
        .ifPresent(user -> {
          throw new RuntimeException("User already exists.");
        });
  }

  @Override
  public void sendVerificationEmail(SendVerificationEmailRequestDto dto) {

    userRepository.findByUsername(dto.getUsername())
        .ifPresent(user -> {
          throw new RuntimeException("User already exists.");
        });

    userRepository.findByEmail(dto.getEmail())
        .ifPresent(user -> {
          throw new RuntimeException("Email already exists.");
        });

    String verificationNumber = VerificationNumberGenerator.generateVerificationNumber();
    boolean sendMail = emailProvider.sendVerificationEmail(dto.getEmail(), (verificationNumber));

    if (!sendMail) {
      throw new RuntimeException("Email send failure.");
    }

    VerificationEntity verificationEntity = VerificationEntity.of(dto.getUsername(), dto.getEmail(), verificationNumber);
    verificationRepository.save(verificationEntity);
  }

  @Override
  public void checkVerification(CheckVerificationRequestDto dto) {

    String username = dto.getUsername();
    String email = dto.getEmail();
    String verificationNumber = dto.getVerificationNumber();

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
