package project.realtimechatapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.realtimechatapplication.dto.request.reaction.AddReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.EditReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.ReactionDto;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.ReactionEntity;
import project.realtimechatapplication.entity.UserEntity;
import project.realtimechatapplication.exception.impl.MessageNotExistException;
import project.realtimechatapplication.exception.impl.ReactionNotFoundException;
import project.realtimechatapplication.exception.impl.UnauthorizedReactionEditException;
import project.realtimechatapplication.exception.impl.UnauthorizedReactionRemoveException;
import project.realtimechatapplication.exception.impl.UserNotFoundException;
import project.realtimechatapplication.repository.MessageRepository;
import project.realtimechatapplication.repository.ReactionRepository;
import project.realtimechatapplication.repository.UserRepository;
import project.realtimechatapplication.service.ReactionService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionServiceImpl implements ReactionService {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReactionRepository reactionRepository;

  @Override
  @Transactional
  public ReactionDto addReaction(AddReactionRequestDto dto, String username) {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);
    MessageEntity message = messageRepository.findById(dto.getMessageId())
        .orElseThrow(MessageNotExistException::new);

    ReactionEntity reaction = ReactionEntity.of(message, user, dto.getReaction());
    reactionRepository.save(reaction);

    return ReactionDto.from(reaction);
  }

  @Override
  @Transactional
  public ReactionDto editReaction(EditReactionRequestDto dto, String username, Long reactionId) {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);
    ReactionEntity reaction = reactionRepository.findById(reactionId)
        .orElseThrow(MessageNotExistException::new);

    if (!reaction.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedReactionEditException();
    }

    reaction.setReaction(dto.getReaction());
    reactionRepository.save(reaction);

    return ReactionDto.from(reaction);
  }

  @Override
  @Transactional
  public void removeReaction(Long reactionId, String username) {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);
    ReactionEntity reaction = reactionRepository.findById(reactionId)
        .orElseThrow(ReactionNotFoundException::new);

    if (!reaction.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedReactionRemoveException();
    }

    reactionRepository.delete(reaction);
  }

  @Override
  public List<ReactionDto> getReactionsByMessageId(Long messageId) {
    MessageEntity message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotExistException::new);

    List<ReactionEntity> reactions = reactionRepository.findByMessage(message);
    return reactions.stream().map(ReactionDto::from).collect(Collectors.toList());
  }
}
