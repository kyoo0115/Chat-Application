package project.realtimechatapplication.service;

import java.util.List;
import org.apache.catalina.User;
import project.realtimechatapplication.dto.request.chat.ChatDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;

public interface ChatRoomService {
    ChatRoomDto createChatRoom(ChatRoomCreateRequestDto dto, String username);

    List<ChatRoomDto> searchChatRoomList(String username);

    ChatRoomDto addMemberToChatRoom(ChatRoomAddRequestDto dto);
}
