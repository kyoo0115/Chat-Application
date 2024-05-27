package project.realtimechatapplication.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.realtimechatapplication.dto.request.auth.SendVerificationEmailRequestDto;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.VerificationEntity;
import project.realtimechatapplication.provider.EmailProvider;
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

  @InjectMocks
  private AuthServiceImpl authService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testUsernameCheck_UserExists() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("existingUser");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.usernameCheck(requestDto);
    });
    assertEquals("User already exists.", exception.getMessage());
  }

  @Test
  public void testUsernameCheck_UserDoesNotExist() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("newUser");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    assertDoesNotThrow(() -> {
      authService.usernameCheck(requestDto);
    });
  }

  @Test
  public void testUsernameCheck_DatabaseError() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("anyUser");
    when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.usernameCheck(requestDto);
    });
    assertEquals("Database error", exception.getMessage());
  }


  @Test
  public void testSendVerificationEmail_UserExists() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("existingUser");
    requestDto.setEmail("user@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.sendVerificationEmail(requestDto);
    });
    assertEquals("User already exists.", exception.getMessage());
  }

  @Test
  public void testSendVerificationEmail_EmailExists() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("existing@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.sendVerificationEmail(requestDto);
    });
    assertEquals("Email already exists.", exception.getMessage());
  }

  @Test
  public void testSendVerificationEmail_EmailSendFailure() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("newuser@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(emailProvider.sendVerificationEmail(anyString(), anyString())).thenReturn(false);

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authService.sendVerificationEmail(requestDto);
    });
    assertEquals("Email send failure.", exception.getMessage());
  }

  @Test
  public void testSendVerificationEmail_Success() {
    // Arrange
    SendVerificationEmailRequestDto requestDto = new SendVerificationEmailRequestDto();
    requestDto.setUsername("newUser");
    requestDto.setEmail("newuser@example.com");
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(emailProvider.sendVerificationEmail(anyString(), anyString())).thenReturn(true);

    // Act
    authService.sendVerificationEmail(requestDto);

    // Assert
    verify(userRepository, times(1)).findByUsername(anyString());
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(emailProvider, times(1)).sendVerificationEmail(anyString(), anyString());
    verify(verificationRepository, times(1)).save(ArgumentMatchers.any(VerificationEntity.class));
  }
}
