package project.realtimechatapplication.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.realtimechatapplication.dto.request.auth.UsernameCheckRequestDto;
import project.realtimechatapplication.dto.response.auth.UsernameCheckResponseDto;
import project.realtimechatapplication.model.ResponseCode;
import project.realtimechatapplication.model.ResponseMessage;
import project.realtimechatapplication.repository.UserRepository;

public class AuthServiceImplTest {

  @Mock
  private UserRepository userRepository;

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
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    // Act
    authService.usernameCheck(requestDto);

    // Assert
    assertEquals();

  }

  @Test
  public void testUsernameCheck_UserDoesNotExist() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("newUser");
    when(userRepository.existsByUsername(anyString())).thenReturn(false);

    // Act
    ResponseEntity<? super UsernameCheckResponseDto> response = authService.usernameCheck(requestDto);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success.", ResponseMessage.SUCCESS);
    assertEquals("SU", ResponseCode.SUCCESS);
  }

  @Test
  public void testUsernameCheck_DatabaseError() {
    // Arrange
    UsernameCheckRequestDto requestDto = new UsernameCheckRequestDto();
    requestDto.setUsername("anyUser");
    when(userRepository.existsByUsername(anyString())).thenThrow(new RuntimeException("Database error"));

    // Act
    ResponseEntity<? super UsernameCheckResponseDto> response = authService.usernameCheck(requestDto);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Database error.", ResponseMessage.DATABASE_ERROR);
    assertEquals("DBE", ResponseCode.DATABASE_ERROR);
  }
}
