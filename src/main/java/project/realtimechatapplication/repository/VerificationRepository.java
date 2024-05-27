package project.realtimechatapplication.repository;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.VerificationEntity;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, Long>  {

  Optional<VerificationEntity> findByUsernameAndEmail(String username, String email);

  Optional<VerificationEntity> findByUsername(String username);

  void deleteByUsername(String username);
}
