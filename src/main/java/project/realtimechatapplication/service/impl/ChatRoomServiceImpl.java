package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.ChatRoomMembershipDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomAddRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomCreateRequestDto;
import project.realtimechatapplication.dto.request.chat.ChatRoomDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.entity.elasticsearch.ElasticsearchChatRoomEntity;
import project.realtimechatapplication.exception.impl.ChatRoomNotFoundException;
import project.realtimechatapplication.exception.impl.MemberAlreadyInChatroomException;
import project.realtimechatapplication.exception.impl.MemberNotInChatroomException;
import project.realtimechatapplication.exception.impl.UnauthorizedRoomOwnerException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.repository.elasticsearch.ElasticsearchChatRoomRepository;
import project.realtimechatapplication.repository.jpa.ChatRoomRepository;
import project.realtimechatapplication.repository.jpa.MemberChatRoomRepository;
import project.realtimechatapplication.repository.jpa.UserRepository;
import project.realtimechatapplication.service.ChatRoomService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final MemberChatRoomRepository memberChatRoomRepository;
  private final ElasticsearchChatRoomRepository elasticsearchChatRoomRepository;

  private final ElasticsearchOperations elasticsearchOperations;

  private static final String INDEX_NAME = "chat_rooms";

  @Override
  @Transactional
  public ChatRoomDto createChatRoom(ChatRoomCreateRequestDto dto, String username) {

    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    ChatRoomEntity chatRoom = ChatRoomEntity.of(dto.getRoomName(), user.getUsername());

    chatRoomRepository.save(chatRoom);
    memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, user));

    ElasticsearchChatRoomEntity elasticsearchChatRoom = ElasticsearchChatRoomEntity.from(chatRoom);

    IndexQuery indexQuery = new IndexQueryBuilder()
        .withId(elasticsearchChatRoom.getId().toString())
        .withObject(elasticsearchChatRoom)
        .build();

    elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_NAME));

    return ChatRoomDto.from(chatRoom);
  }

  @Override
  @Transactional
  public ChatRoomDto addMemberToChatRoom(ChatRoomAddRequestDto dto, String username,
      Long memberId) {

    ChatRoomMembershipDto membershipDto = memberChatRoomRepository.checkChatRoomAndUserDetails(
            dto.getRoomCode(), username, memberId)
        .orElseThrow(ChatRoomNotFoundException::new);

    validateRoomOwner(membershipDto, username);
    validateMemberNotInChatRoom(membershipDto);

    UserEntity member = userRepository.findById(memberId)
        .orElseThrow(UserNotFoundException::new);

    ChatRoomEntity chatRoom = chatRoomRepository.getReferenceById(membershipDto.getChatRoomId());
    memberChatRoomRepository.save(MemberChatRoomEntity.of(chatRoom, member));

    return ChatRoomDto.from(chatRoom);
  }

  @Override
  @Transactional
  public ChatRoomDto deleteChatRoom(Long roomId, String username) {

    ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(ChatRoomNotFoundException::new);

    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    if (!chatRoom.getOwner().equals(user.getUsername())) {
      throw new UnauthorizedRoomOwnerException();
    }

    chatRoomRepository.delete(chatRoom);
    elasticsearchChatRoomRepository.deleteById(roomId);

    return ChatRoomDto.from(chatRoom);
  }

  @Override
  public List<ElasticsearchChatRoomEntity> searchChatRooms(String query) {
    return elasticsearchChatRoomRepository.findByNameContaining(query);
  }

  @Override
  @Transactional
  public ChatRoomDto removeMemberFromChatRoom(Long userId, Long roomId, String username) {

    ChatRoomMembershipDto membershipDto = memberChatRoomRepository.checkChatRoomAndUserDetailsById(
            roomId, username, userId)
        .orElseThrow(ChatRoomNotFoundException::new);

    validateRoomOwner(membershipDto, username);
    validateMemberInChatRoom(membershipDto);

    UserEntity member = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    MemberChatRoomEntity memberChatRoom = memberChatRoomRepository.findByUserAndChatRoom(member,
            chatRoomRepository.getReferenceById(roomId))
        .orElseThrow(MemberNotInChatroomException::new);
    memberChatRoomRepository.delete(memberChatRoom);

    ChatRoomEntity chatRoom = chatRoomRepository.getReferenceById(membershipDto.getChatRoomId());

    return ChatRoomDto.from(chatRoom);
  }

  @Override
  public List<ChatRoomDto> searchChatRoomList(String username) {

    List<ChatRoomEntity> chatRooms = memberChatRoomRepository.findChatRoomsByUsername(username);

    return chatRooms.stream()
        .map(ChatRoomDto::from)
        .collect(Collectors.toList());
  }

  private void validateRoomOwner(ChatRoomMembershipDto membershipDto, String username) {
    if (!membershipDto.getOwnerUsername().equals(username)) {
      throw new UnauthorizedRoomOwnerException();
    }
  }

  private void validateMemberNotInChatRoom(ChatRoomMembershipDto membershipDto) {
    if (membershipDto.isMemberAlreadyInChatRoom()) {
      throw new MemberAlreadyInChatroomException();
    }
  }

  private void validateMemberInChatRoom(ChatRoomMembershipDto membershipDto) {
    if (!membershipDto.isMemberAlreadyInChatRoom()) {
      throw new MemberNotInChatroomException();
    }
  }
}
