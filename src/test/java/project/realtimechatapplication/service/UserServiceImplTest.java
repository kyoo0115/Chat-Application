package project.realtimechatapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.realtimechatapplication.dto.request.user.EditUserRequestDto;
import project.realtimechatapplication.dto.request.user.UserDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.UnauthorizedUserDeleteException;
import project.realtimechatapplication.exception.impl.UnauthorizedUserEditException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.impl.UserServiceImpl;

public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetUserById_Success() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    UserDto response = userService.getUserById(1L);

    verify(userRepository).findById(1L);

    assertEquals("testUser", response.getUsername());
  }

  @Test
  public void testGetUserById_UserNotFound() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
  }

  @Test
  public void testEditUser_Success() {
    EditUserRequestDto requestDto = new EditUserRequestDto();
    requestDto.setName("New Name");

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    UserDto response = userService.editUser(1L, requestDto, "testUser");

    verify(userRepository).findById(1L);
    verify(userRepository).save(user);

    assertEquals("New Name", response.getName());
  }

  @Test
  public void testEditUser_UnauthorizedEdit() {
    EditUserRequestDto requestDto = new EditUserRequestDto();
    requestDto.setName("New Name");

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    assertThrows(UnauthorizedUserEditException.class,
        () -> userService.editUser(1L, requestDto, "anotherUser"));
  }

  @Test
  public void testEditUser_UserNotFound() {
    EditUserRequestDto requestDto = new EditUserRequestDto();
    requestDto.setName("New Name");

    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.editUser(1L, requestDto, "testUser"));
  }

  @Test
  public void testDeleteUser_Success() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    userService.deleteUser(1L, "testUser");

    verify(userRepository).findById(1L);
    verify(userRepository).delete(user);
  }

  @Test
  public void testDeleteUser_UnauthorizedDelete() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    assertThrows(UnauthorizedUserDeleteException.class,
        () -> userService.deleteUser(1L, "anotherUser"));
  }

  @Test
  public void testDeleteUser_UserNotFound() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L, "testUser"));
  }

  @Test
  public void testSearchChatRoomMembers_Success() {
    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setRoomCode("ROOM123");

    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setUsername("testUser");

    when(chatRoomRepository.findByRoomCode(anyString())).thenReturn(Optional.of(chatRoom));
    when(userRepository.findByUsernameStartingWithAndChatRooms(anyString(),
        any(ChatRoomEntity.class)))
        .thenReturn(Collections.singletonList(user));

    List<UserDto> response = userService.searchChatRoomMembers("ROOM123", "test");

    verify(chatRoomRepository).findByRoomCode("ROOM123");
    verify(userRepository).findByUsernameStartingWithAndChatRooms("test", chatRoom);

    assertEquals(1, response.size());
    assertEquals("testUser", response.get(0).getUsername());
  }

  @Test
  public void testSearchChatRoomMembers_ChatRoomNotFound() {
    when(chatRoomRepository.findByRoomCode(anyString())).thenReturn(Optional.empty());

    assertThrows(ChatRoomNotFoundException.class,
        () -> userService.searchChatRoomMembers("ROOM123", "test"));
  }
}
