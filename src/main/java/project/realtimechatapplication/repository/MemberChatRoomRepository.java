package project.realtimechatapplication.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;

@Repository
public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoomEntity, Long> {

  @Query("SELECT cr FROM MemberChatRoomEntity mcr JOIN mcr.chatRoom cr WHERE mcr.user.username = :username")
  List<ChatRoomEntity> findChatRoomsByUsername(@Param("username") String username);

  Optional<MemberChatRoomEntity> findByUserAndChatRoom(UserEntity user, ChatRoomEntity chatRoom);
}
