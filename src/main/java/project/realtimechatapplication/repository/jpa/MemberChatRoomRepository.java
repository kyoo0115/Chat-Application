package project.realtimechatapplication.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.dto.ChatRoomMembershipDto;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;

@Repository
public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoomEntity, Long> {

  @Query("SELECT cr FROM MemberChatRoomEntity mcr JOIN mcr.chatRoom cr WHERE mcr.user.username = :username")
  List<ChatRoomEntity> findChatRoomsByUsername(@Param("username") String username);

  @Query("SELECT new project.realtimechatapplication.dto.ChatRoomMembershipDto(" +
      "cr.id, u.username, cr.owner, " +
      "(SELECT COUNT(m.id) FROM MemberChatRoomEntity m WHERE m.user.id = :memberId AND m.chatRoom.id = cr.id) > 0) "
      +
      "FROM ChatRoomEntity cr " +
      "JOIN cr.memberChatRooms m " +
      "JOIN m.user u " +
      "WHERE cr.roomCode = :roomCode AND u.username = :username")
  Optional<ChatRoomMembershipDto> checkChatRoomAndUserDetails(@Param("roomCode") String roomCode,
      @Param("username") String username,
      @Param("memberId") Long memberId);

  @Query("SELECT new project.realtimechatapplication.dto.ChatRoomMembershipDto(" +
      "cr.id, u.username, cr.owner, " +
      "(SELECT COUNT(m.id) FROM MemberChatRoomEntity m WHERE m.user.id = :memberId AND m.chatRoom.id = cr.id) > 0) "
      +
      "FROM ChatRoomEntity cr " +
      "JOIN cr.memberChatRooms m " +
      "JOIN m.user u " +
      "WHERE cr.id = :roomId AND u.username = :username")
  Optional<ChatRoomMembershipDto> checkChatRoomAndUserDetailsById(@Param("roomId") Long roomId,
      @Param("username") String username,
      @Param("memberId") Long memberId);

  Optional<MemberChatRoomEntity> findByUserAndChatRoom(UserEntity member,
      ChatRoomEntity referenceById);
}
