package project.realtimechatapplication.service;

import java.util.List;
import project.realtimechatapplication.dto.request.user.EditUserRequestDto;
import project.realtimechatapplication.dto.request.user.UserDto;

public interface UserService {

  UserDto getUserById(Long userId);

  UserDto editUser(Long userId, EditUserRequestDto dto, String username);

  void deleteUser(Long userId, String username);

  List<UserDto> searchChatRoomMembers(String roomCode, String query);
}
