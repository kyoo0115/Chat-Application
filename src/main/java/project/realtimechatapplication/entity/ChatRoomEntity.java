package project.realtimechatapplication.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Table(name = "chatRoom")
@Entity
@Getter
public class ChatRoomEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = LAZY)
  private UserEntity user;

  @Column(nullable = false)
  private Boolean isPrivate;

  @OneToMany(mappedBy = "chatRoom", cascade = ALL, fetch = LAZY)
  private List<MessageEntity> messages = new ArrayList<>();

}

