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
import lombok.Getter;
import lombok.Setter;

@Table(name = "user")
@Entity
@Getter
@Setter
public class UserEntity extends BaseEntity{
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username; //로그인 아이디

  @Column(unique = true, nullable = false)
  private String email; //이메일 인증을 위해 동작

  @Column(nullable = false)
  private String name; //채팅에 보여줄 닉네임

  @Column(nullable = false)
  private String password;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Column(nullable = false)
  private LocalDate birthDate;

  @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
  private List<MessageEntity> messages = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
  private List<NotificationEntity> notifications = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = ALL, fetch = LAZY)
  private List<ChatRoomEntity> chatRooms = new ArrayList<>();

}
