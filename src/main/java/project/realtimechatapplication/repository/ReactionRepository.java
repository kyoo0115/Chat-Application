package project.realtimechatapplication.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.ReactionEntity;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {

  List<ReactionEntity> findByMessage(MessageEntity message);
}
