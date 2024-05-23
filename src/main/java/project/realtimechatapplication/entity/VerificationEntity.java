package project.realtimechatapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "verification")
@Table(name = "verification")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEntity extends BaseEntity {

  @Id
  private long userId;
  private String email;
  private String certificationNumber;
}

