package project.realtimechatapplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.realtimechatapplication.dto.request.reaction.AddReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.EditReactionRequestDto;
import project.realtimechatapplication.dto.request.reaction.ReactionDto;
import project.realtimechatapplication.dto.response.CustomResponse;
import project.realtimechatapplication.service.ReactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
public class ReactionController {

  private final ReactionService reactionService;

  @PostMapping
  public ResponseEntity<?> addReaction(
      @RequestBody @Valid AddReactionRequestDto dto,
      @AuthenticationPrincipal final User user
  ) {
    ReactionDto reaction = reactionService.addReaction(dto, user.getUsername());
    return CustomResponse.success(reaction);
  }

  @PutMapping("/{reactionId}")
  public ResponseEntity<?> editReaction(
      @PathVariable Long reactionId,
      @RequestBody @Valid EditReactionRequestDto dto,
      @AuthenticationPrincipal final User user
  ) {
    ReactionDto reaction = reactionService.editReaction(dto, user.getUsername(), reactionId);
    return CustomResponse.success(reaction);
  }

  @DeleteMapping("/{reactionId}")
  public ResponseEntity<?> removeReaction(
      @PathVariable Long reactionId,
      @AuthenticationPrincipal User user
  ) {
    reactionService.removeReaction(reactionId, user.getUsername());
    return CustomResponse.success();
  }

  @GetMapping("/message/{messageId}")
  public ResponseEntity<?> getReactionsByMessageId(@PathVariable Long messageId) {
    List<ReactionDto> reactions = reactionService.getReactionsByMessageId(messageId);
    return CustomResponse.success(reactions);
  }
}
