package project.realtimechatapplication.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import project.realtimechatapplication.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  
}
