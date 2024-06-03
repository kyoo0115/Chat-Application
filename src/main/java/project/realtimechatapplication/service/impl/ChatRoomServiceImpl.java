package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.repository.ChatRoomRepository;
import project.realtimechatapplication.repository.MemberChatRoomRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.ChatRoomService;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    @Override
    @Transactional
    public ChatRoomDto createChatRoom(String roomName, String username) {

        ChatRoomEntity chatRoom = ChatRoomEntity.of(roomName);

        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        chatRoomRepository.save(chatRoom);
        memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, user));

        return ChatRoomDto.from(chatRoom);
    }

    @Override
    @Transactional
    public ChatRoomDto addMemberToChatRoom(String roomCode, String username, String memberToAdd) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        UserEntity member = userRepository.findByUsername(memberToAdd)
            .orElseThrow(UserNotFoundException::new);

        if (memberChatRoomRepository.findByUserAndChatRoom(member, chatRoom).isEmpty()) {
            memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, member));
        }

        return ChatRoomDto.from(chatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> searchChatRoomList(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        List<MemberChatRoomEntity> memberChatRoomList = memberChatRoomRepository.findByUser(user);

        return memberChatRoomList.stream()
            .map(memberChatRoom -> ChatRoomDto.from(memberChatRoom.getChatRoom()))
            .collect(Collectors.toList());
    }
}
