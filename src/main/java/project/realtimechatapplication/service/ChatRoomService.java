package project.realtimechatapplication.service;

import java.util.List;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;

public interface ChatRoomService {
    ChatRoomDto createChatRoom(String roomName, String username);

    List<ChatRoomDto> searchChatRoomList(String username);

    ChatRoomDto addMemberToChatRoom(String roomCode, String username, String memberToAdd);
}
