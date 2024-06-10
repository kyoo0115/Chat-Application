package project.realtimechatapplication.service;

import java.util.List;
import project.realtimechatapplication.dto.request.reaction.AddReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.EditReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.ReactionDto;

public interface ReactionService {

  ReactionDto addReaction(AddReactionRequestDto dto, String username);

  ReactionDto editReaction(EditReactionRequestDto dto, String username, Long reactionId);

  void removeReaction(Long reactionId, String username);

  List<ReactionDto> getReactionsByMessageId(Long messageId);
}
