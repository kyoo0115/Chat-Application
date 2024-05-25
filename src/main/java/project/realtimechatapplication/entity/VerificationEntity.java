package project.realtimechatapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "verification")
@Table(name = "verification")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationEntity extends BaseEntity {

  @Id
  private String username;

  private String email;
  private String verificationNumber;

  public static VerificationEntity of(String username, String email, String verificationNumber) {
    return VerificationEntity.builder()
        .username(username)
        .email(email)
        .verificationNumber(verificationNumber)
        .build();
  }
}

