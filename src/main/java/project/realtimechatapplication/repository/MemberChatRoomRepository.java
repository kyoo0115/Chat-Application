package project.realtimechatapplication.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.MemberChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;

@Repository
public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoomEntity, Long> {
  List<MemberChatRoomEntity> findByUser(UserEntity user);

  Optional<MemberChatRoomEntity> findByUserAndChatRoom(UserEntity user, ChatRoomEntity chatRoom);
}
