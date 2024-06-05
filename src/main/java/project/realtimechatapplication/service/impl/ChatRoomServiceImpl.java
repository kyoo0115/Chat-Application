package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
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
    public ChatRoomDto createChatRoom(ChatRoomCreateRequestDto dto, String username) {

        ChatRoomEntity chatRoom = ChatRoomEntity.of(dto.getRoomName());

        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        chatRoomRepository.save(chatRoom);
        memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, user));

        return ChatRoomDto.from(chatRoom);
    }

    @Override
    @Transactional
    public ChatRoomDto addMemberToChatRoom(ChatRoomAddRequestDto dto) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomCode(dto.getRoomCode())
            .orElseThrow(ChatRoomNotFoundException::new);

        UserEntity member = userRepository.findByUsername(dto.getUsername())
            .orElseThrow(UserNotFoundException::new);

        if (memberChatRoomRepository.findByUserAndChatRoom(member, chatRoom).isEmpty()) {
            memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, member));
        }

        return ChatRoomDto.from(chatRoom);
    }

    @Override
    @Transactional(readOnly = true) //쿼리 한번으로 최적화 성공
    public List<ChatRoomDto> searchChatRoomList(String username) {

        List<ChatRoomEntity> chatRooms = memberChatRoomRepository.findChatRoomsByUsername(username);

        return chatRooms.stream()
            .map(ChatRoomDto::from)
            .collect(Collectors.toList());
    }
}
