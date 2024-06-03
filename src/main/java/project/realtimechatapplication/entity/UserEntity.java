package project.realtimechatapplication.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.dto.request.auth.SignUpRequestDto;

@Table(name = "user")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends TimeStamped {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Column(nullable = false)
  private LocalDate birthDate;

  @Column
  private String role;

  @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
  private List<NotificationEntity> notifications = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
  private List<MemberChatRoomEntity> memberChatRooms = new ArrayList<>();

  public static UserEntity of(SignUpRequestDto dto) {
    return UserEntity.builder()
        .username(dto.getUsername())
        .email(dto.getEmail())
        .name(dto.getName())
        .password(dto.getPassword())
        .birthDate(dto.getBirthDate())
        .role("ROLE_USER")
        .build();
  }
}
