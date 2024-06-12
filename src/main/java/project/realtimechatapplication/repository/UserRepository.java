package project.realtimechatapplication.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.ChatRoomEntity;
import project.realtimechatapplication.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String username);

  Optional<Object> findByEmail(String email);

  @Query("SELECT u FROM UserEntity u JOIN MemberChatRoomEntity m ON u.id = m.user.id WHERE m.chatRoom = :chatRoom AND u.username LIKE :query%")
  List<UserEntity> findByUsernameStartingWithAndChatRooms(@Param("query") String query,
      @Param("chatRoom") ChatRoomEntity chatRoom);

}
