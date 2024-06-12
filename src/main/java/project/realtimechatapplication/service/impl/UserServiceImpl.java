package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import project.realtimechatapplication.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;

  @Override
  public UserDto getUserById(Long userId) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    return UserDto.from(user);
  }

  @Override
  @Transactional
  public UserDto editUser(Long userId, EditUserRequestDto dto, String username) {
    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    if (!userEntity.getUsername().equals(username)) {
      throw new UnauthorizedUserEditException();
    }

    userEntity.setName(dto.getName());

    userRepository.save(userEntity);
    return UserDto.from(userEntity);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId, String username) {
    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    if (!userEntity.getUsername().equals(username)) {
      throw new UnauthorizedUserDeleteException();
    }

    userRepository.delete(userEntity);
  }

  @Override
  public List<UserDto> searchChatRoomMembers(String roomCode, String query) {
    ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(roomCode)
        .orElseThrow(ChatRoomNotFoundException::new);

    List<UserEntity> users = userRepository.findByUsernameStartingWithAndChatRooms(query, chatRoom);

    return users.stream().map(UserDto::from).collect(
        Collectors.toList());
  }
}
