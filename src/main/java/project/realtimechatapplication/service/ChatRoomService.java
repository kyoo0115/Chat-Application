package project.realtimechatapplication.service;

import java.util.List;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;

public interface ChatRoomService {

  ChatRoomDto createChatRoom(ChatRoomCreateRequestDto dto, String username);

  List<ChatRoomDto> searchChatRoomList(String username);

  ChatRoomDto addMemberToChatRoom(ChatRoomAddRequestDto dto, String username, Long memberId);

  ChatRoomDto removeMemberFromChatRoom(Long userId, Long roomId, String username);

  ChatRoomDto deleteChatRoom(Long roomId, String username);
}
