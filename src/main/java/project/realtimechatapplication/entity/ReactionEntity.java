package project.realtimechatapplication.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.model.type.Reaction;

@Table(name = "reaction")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactionEntity extends TimeStamped {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "message_id", nullable = false)
  private MessageEntity message;

  public static ReactionEntity of(MessageEntity message, UserEntity user, Reaction reaction) {
   return ReactionEntity.builder()
       .message(message)
       .user(user)
       .reaction(reaction)
       .build();
  }
}
