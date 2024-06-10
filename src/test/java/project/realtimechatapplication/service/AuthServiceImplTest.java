package project.realtimechatapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import project.realtimechatapplication.exception.impl.EmailSendErrorException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.exception.impl.UsernameAlreadyExistsException;
import project.realtimechatapplication.exception.impl.VerificationNumberNotMatchedException;
import project.realtimechatapplication.exception.impl.WrongPasswordException;
import project.realtimechatapplication.provider.EmailProvider;
import project.realtimechatapplication.provider.TokenProvider;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.repository.VerificationRepository;
import project.realtimechatapplication.service.impl.AuthServiceImpl;

public class AuthServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private VerificationRepository verificationRepository;

  @Mock
  private EmailProvider emailProvider;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TokenProvider tokenProvider;

  @Captor
  private ArgumentCaptor<String> stringCaptor;

  @InjectMocks
  private AuthServiceImpl authService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testUsernameCheck_Success() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("newUser");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // Act
    authService.usernameCheck(requestDto);

    // Assert
    verify(userRepository).findByUsername(requestDto.getUsername());
  }

  @Test
  public void testUsernameCheck_Fail_UsernameAlreadyExists() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("existingUser");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    assertThrows(UsernameAlreadyExistsException.class, () -> authService.usernameCheck(requestDto));
  }

  @Test
  public void testSendVerificationEmail_Success() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    // Act
    authService.sendVerificationEmail(requestDto);

    // Assert
    verify(userRepository).findByUsername(requestDto.getUsername());
    verify(userRepository).findByEmail(requestDto.getEmail());
    verify(emailProvider).sendVerificationEmail(eq(requestDto.getEmail()), anyString());
    verify(verificationRepository).save(any(VerificationEntity.class));
  }

  @Test
  public void testSendVerificationEmail_Fail_EmailSendError() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    doThrow(new EmailSendErrorException()).when(emailProvider).sendVerificationEmail(anyString(), anyString());

    // Act & Assert
    assertThrows(EmailSendErrorException.class, () -> authService.sendVerificationEmail(requestDto));
  }

  @Test
  public void testCheckVerification_Success() {
    // Arrange
    CheckVerificationRequestDto requestDto = new CheckVerificationRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail(requestDto.getEmail());
    verificationEntity.setVerificationNumber(requestDto.getVerificationNumber());
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));

    // Act
    authService.checkVerification(requestDto);

    // Assert
    verify(verificationRepository).findByUsername(requestDto.getUsername());
  }

  @Test
  public void testCheckVerification_Fail_UserNotFound() {
    // Arrange
    CheckVerificationRequestDto requestDto = new CheckVerificationRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> authService.checkVerification(requestDto));
  }

  @Test
  public void testCheckVerification_Fail_EmailNotMatched() {
    // Arrange
    CheckVerificationRequestDto requestDto = new CheckVerificationRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail("different.email@example.com");
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));

    // Act & Assert
    assertThrows(EmailNotMatchedException.class, () -> authService.checkVerification(requestDto));
  }

  @Test
  public void testCheckVerification_Fail_VerificationNumberNotMatched() {
    // Arrange
    CheckVerificationRequestDto requestDto = new CheckVerificationRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail(requestDto.getEmail());
    verificationEntity.setVerificationNumber("654321");
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));

    // Act & Assert
    assertThrows(VerificationNumberNotMatchedException.class, () -> authService.checkVerification(requestDto));
  }

  @Test
  public void testSignUp_Success() {
    // Arrange
    SignUpRequestDto requestDto = new SignUpRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setPassword("password");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail(requestDto.getEmail());
    verificationEntity.setVerificationNumber(requestDto.getVerificationNumber());
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

    // Act
    authService.signUp(requestDto);

    // Assert
    verify(userRepository).findByUsername(requestDto.getUsername());
    verify(userRepository).findByEmail(requestDto.getEmail());
    verify(verificationRepository).findByUsername(requestDto.getUsername());
    verify(passwordEncoder).encode(stringCaptor.capture());
    verify(userRepository).save(any(UserEntity.class));
    verify(verificationRepository).deleteByUsername(requestDto.getUsername());

    // Verify the captured value
    assertEquals("password", stringCaptor.getValue());
    assertEquals("encodedPassword", passwordEncoder.encode(requestDto.getPassword()));
  }

  @Test
  public void testSignUp_Fail_UsernameAlreadyExists() {
    // Arrange
    SignUpRequestDto requestDto = new SignUpRequestDto();
    requestDto.setUsername("existingUser");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    assertThrows(UsernameAlreadyExistsException.class, () -> authService.signUp(requestDto));
  }

  @Test
  public void testSignUp_Fail_EmailAlreadyExists() {
    // Arrange
    SignUpRequestDto requestDto = new SignUpRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("existing.email@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    assertThrows(EmailAlreadyExistsException.class, () -> authService.signUp(requestDto));
  }

  @Test
  public void testSignUp_Fail_EmailNotMatched() {
    // Arrange
    SignUpRequestDto requestDto = new SignUpRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail("different.email@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));

    // Act & Assert
    assertThrows(EmailNotMatchedException.class, () -> authService.signUp(requestDto));
  }

  @Test
  public void testSignUp_Fail_VerificationNumberNotMatched() {
    // Arrange
    SignUpRequestDto requestDto = new SignUpRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("new.user@example.com");
    requestDto.setVerificationNumber("123456");
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setEmail(requestDto.getEmail());
    verificationEntity.setVerificationNumber("654321");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(verificationRepository.findByUsername(anyString())).thenReturn(Optional.of(verificationEntity));

    // Act & Assert
    assertThrows(VerificationNumberNotMatchedException.class, () -> authService.signUp(requestDto));
  }

  @Test
  public void testSignIn_Success() {
    // Arrange
    SignInRequestDto requestDto = new SignInRequestDto();
    requestDto.setUsername("testUser");
    requestDto.setPassword("testPassword");

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("testUser");
    userEntity.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(tokenProvider.createToken(anyString())).thenReturn("testToken");

    // Act
    SignInResponseDto response = authService.signIn(requestDto);

    // Assert
    verify(userRepository).findByUsername(requestDto.getUsername());
    verify(passwordEncoder).matches(requestDto.getPassword(), userEntity.getPassword());
    verify(tokenProvider).createToken(requestDto.getUsername());

    assertEquals("testToken", Objects.requireNonNull(response.getBody()).getToken());
    assertEquals(3600, response.getBody().getExpirationTime());
  }

  @Test
  public void testSignIn_Fail_UserNotFound() {
    // Arrange
    SignInRequestDto requestDto = new SignInRequestDto();
    requestDto.setUsername("nonExistentUser");
    requestDto.setPassword("testPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> authService.signIn(requestDto));
    verify(userRepository).findByUsername(requestDto.getUsername());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
    verify(tokenProvider, never()).createToken(anyString());
  }

  @Test
  public void testSignIn_Fail_WrongPassword() {
    // Arrange
    SignInRequestDto requestDto = new SignInRequestDto();
    requestDto.setUsername("testUser");
    requestDto.setPassword("wrongPassword");

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("testUser");
    userEntity.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    // Act & Assert
    assertThrows(WrongPasswordException.class, () -> authService.signIn(requestDto));
    verify(userRepository).findByUsername(requestDto.getUsername());
    verify(passwordEncoder).matches(requestDto.getPassword(), userEntity.getPassword());
    verify(tokenProvider, never()).createToken(anyString());
  }
}
