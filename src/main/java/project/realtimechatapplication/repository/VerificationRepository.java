package project.realtimechatapplication.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.VerificationEntity;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {

  Optional<VerificationEntity> findByUsername(String username);

  void deleteByUsername(String username);
}
