package project.realtimechatapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.realtimechatapplication.entity.VerificationEntity;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, Long>  {

}
