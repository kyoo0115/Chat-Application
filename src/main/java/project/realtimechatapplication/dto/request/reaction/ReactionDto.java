package project.realtimechatapplication.dto.request.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.realtimechatapplication.entity.ReactionEntity;
import project.realtimechatapplication.model.type.Reaction;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReactionDto {

  private Long id;
  private Reaction reaction;
  private Long userId;
  private Long messageId;

  public static ReactionDto from(ReactionEntity reaction) {
    return ReactionDto.builder()
        .id(reaction.getId())
        .reaction(reaction.getReaction())
        .userId(reaction.getUser().getId())
        .messageId(reaction.getMessage().getId())
        .build();
  }
}
