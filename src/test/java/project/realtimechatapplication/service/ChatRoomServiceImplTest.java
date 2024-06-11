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
import project.realtimechatapplication.dto.ChatRoomMembershipDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MemberAlreadyInChatroomException;
import project.realtimechatapplication.exception.impl.MemberNotInChatroomException;
import project.realtimechatapplication.exception.impl.UnauthorizedRoomOwnerException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.MemberChatRoomRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.impl.ChatRoomServiceImpl;

public class ChatRoomServiceTest {

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MemberChatRoomRepository memberChatRoomRepository;

  @InjectMocks
  private ChatRoomServiceImpl chatRoomService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateChatRoom_Success() {
    ChatRoomCreateRequestDto requestDto = new ChatRoomCreateRequestDto();
    requestDto.setRoomName("Test Room");
    UserEntity user = new UserEntity();
    user.setUsername("testUser");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(chatRoomRepository.save(any(ChatRoomEntity.class))).thenReturn(new ChatRoomEntity());

    ChatRoomDto response = chatRoomService.createChatRoom(requestDto, "testUser");

    verify(userRepository).findByUsername("testUser");
    verify(chatRoomRepository).save(any(ChatRoomEntity.class));
    verify(memberChatRoomRepository).save(any(MemberChatRoomEntity.class));

    assertEquals(requestDto.getRoomName(), response.getRoomName());
  }

  @Test
  public void testCreateChatRoom_Fail_UserNotFound() {
    ChatRoomCreateRequestDto requestDto = new ChatRoomCreateRequestDto();
    requestDto.setRoomName("Test Room");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> chatRoomService.createChatRoom(requestDto, "testUser"));
  }

  @Test
  public void testAddMemberToChatRoom_Success() {
    ChatRoomAddRequestDto requestDto = new ChatRoomAddRequestDto();
    requestDto.setRoomCode("ROOM123");

    ChatRoomMembershipDto membershipDto = new ChatRoomMembershipDto();
    membershipDto.setChatRoomId(1L);
    membershipDto.setOwnerUsername("ownerUser");
    membershipDto.setMemberAlreadyInChatRoom(false);

    UserEntity member = new UserEntity();
    member.setId(1L);

    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setId(1L); // Ensure ID is set

    when(memberChatRoomRepository.checkChatRoomAndUserDetails(anyString(), anyString(), anyLong()))
        .thenReturn(Optional.of(membershipDto));
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(member));
    when(chatRoomRepository.getReferenceById(anyLong())).thenReturn(chatRoom);

    ChatRoomDto response = chatRoomService.addMemberToChatRoom(requestDto, "ownerUser", 1L);

    verify(memberChatRoomRepository).checkChatRoomAndUserDetails("ROOM123", "ownerUser", 1L);
    verify(userRepository).findById(1L);
    verify(memberChatRoomRepository).save(any(MemberChatRoomEntity.class));

    assertEquals(1L, response.getId());
  }


  @Test
  public void testAddMemberToChatRoom_Fail_ChatRoomNotFound() {
    ChatRoomAddRequestDto requestDto = new ChatRoomAddRequestDto();
    requestDto.setRoomCode("ROOM123");

    when(memberChatRoomRepository.checkChatRoomAndUserDetails(anyString(), anyString(), anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(ChatRoomNotFoundException.class, () -> chatRoomService.addMemberToChatRoom(requestDto, "ownerUser", 1L));
  }

  @Test
  public void testAddMemberToChatRoom_Fail_MemberAlreadyInChatRoom() {
    ChatRoomAddRequestDto requestDto = new ChatRoomAddRequestDto();
    requestDto.setRoomCode("ROOM123");
    ChatRoomMembershipDto membershipDto = new ChatRoomMembershipDto();
    membershipDto.setChatRoomId(1L);
    membershipDto.setOwnerUsername("ownerUser");
    membershipDto.setMemberAlreadyInChatRoom(true);

    when(memberChatRoomRepository.checkChatRoomAndUserDetails(anyString(), anyString(), anyLong()))
        .thenReturn(Optional.of(membershipDto));

    assertThrows(MemberAlreadyInChatroomException.class, () -> chatRoomService.addMemberToChatRoom(requestDto, "ownerUser", 1L));
  }

  @Test
  public void testDeleteChatRoom_Success() {
    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setOwner("testUser");
    chatRoom.setName("Test Room");

    UserEntity user = new UserEntity();
    user.setUsername("testUser");

    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    ChatRoomDto response = chatRoomService.deleteChatRoom(1L, "testUser");

    verify(chatRoomRepository).findById(1L);
    verify(chatRoomRepository).delete(chatRoom);

    assertEquals(chatRoom.getName(), response.getRoomName());
  }


  @Test
  public void testDeleteChatRoom_Fail_ChatRoomNotFound() {
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ChatRoomNotFoundException.class, () -> chatRoomService.deleteChatRoom(1L, "testUser"));
  }

  @Test
  public void testDeleteChatRoom_Fail_UnauthorizedRoomOwner() {
    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setOwner("differentUser");

    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

    assertThrows(UnauthorizedRoomOwnerException.class, () -> chatRoomService.deleteChatRoom(1L, "testUser"));
  }

  @Test
  public void testRemoveMemberFromChatRoom_Success() {
    ChatRoomMembershipDto membershipDto = new ChatRoomMembershipDto();
    membershipDto.setChatRoomId(1L);
    membershipDto.setOwnerUsername("ownerUser");
    membershipDto.setMemberAlreadyInChatRoom(true);
    UserEntity member = new UserEntity();
    ChatRoomEntity chatRoom = new ChatRoomEntity();
    MemberChatRoomEntity memberChatRoom = new MemberChatRoomEntity();

    when(memberChatRoomRepository.checkChatRoomAndUserDetailsById(anyLong(), anyString(), anyLong()))
        .thenReturn(Optional.of(membershipDto));
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(member));
    when(memberChatRoomRepository.findByUserAndChatRoom(any(UserEntity.class), any(ChatRoomEntity.class)))
        .thenReturn(Optional.of(memberChatRoom));
    when(chatRoomRepository.getReferenceById(anyLong())).thenReturn(chatRoom);

    ChatRoomDto response = chatRoomService.removeMemberFromChatRoom(1L, 1L, "ownerUser");

    verify(memberChatRoomRepository).checkChatRoomAndUserDetailsById(1L, "ownerUser", 1L);
    verify(userRepository).findById(1L);
    verify(memberChatRoomRepository).delete(memberChatRoom);

    assertEquals(chatRoom.getName(), response.getRoomName());
  }

  @Test
  public void testRemoveMemberFromChatRoom_Fail_ChatRoomNotFound() {
    when(memberChatRoomRepository.checkChatRoomAndUserDetailsById(anyLong(), anyString(), anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(ChatRoomNotFoundException.class, () -> chatRoomService.removeMemberFromChatRoom(1L, 1L, "ownerUser"));
  }

  @Test
  public void testRemoveMemberFromChatRoom_Fail_MemberNotInChatRoom() {
    ChatRoomMembershipDto membershipDto = new ChatRoomMembershipDto();
    membershipDto.setChatRoomId(1L);
    membershipDto.setOwnerUsername("ownerUser");
    membershipDto.setMemberAlreadyInChatRoom(false);

    when(memberChatRoomRepository.checkChatRoomAndUserDetailsById(anyLong(), anyString(), anyLong()))
        .thenReturn(Optional.of(membershipDto));

    assertThrows(MemberNotInChatroomException.class, () -> chatRoomService.removeMemberFromChatRoom(1L, 1L, "ownerUser"));
  }

  @Test
  public void testSearchChatRoomList_Success() {
    ChatRoomEntity chatRoom = new ChatRoomEntity();
    chatRoom.setName("Test Room");

    when(memberChatRoomRepository.findChatRoomsByUsername(anyString())).thenReturn(Collections.singletonList(chatRoom));

    List<ChatRoomDto> chatRooms = chatRoomService.searchChatRoomList("testUser");

    assertEquals(1, chatRooms.size());
    assertEquals(chatRoom.getName(), chatRooms.get(0).getRoomName());
  }
}
