package project.realtimechatapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import project.realtimechatapplication.service.NotificationService;
import project.realtimechatapplication.service.ReactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
@Tag(name = "반응 컨트롤러", description = "메시지 반응 관리를 위한 API들")
public class ReactionController {

  private final ReactionService reactionService;
  private final NotificationService notificationService;

  @Operation(summary = "반응 추가", description = "메시지에 반응을 추가합니다.")
  @PostMapping
  public ResponseEntity<?> addReaction(
      @RequestBody @Valid AddReactionRequestDto dto,
      @AuthenticationPrincipal final User user
  ) {
    ReactionDto reaction = reactionService.addReaction(dto, user.getUsername());
    notificationService.createAndSendReactionNotification(dto.getMessageId(), user.getUsername(),
        reaction.getReaction());
    return CustomResponse.success(reaction);
  }

  @Operation(summary = "반응 수정", description = "기존 반응을 수정합니다.")
  @PutMapping("/{reactionId}")
  public ResponseEntity<?> editReaction(
      @PathVariable Long reactionId,
      @RequestBody @Valid EditReactionRequestDto dto,
      @AuthenticationPrincipal final User user
  ) {
    ReactionDto reaction = reactionService.editReaction(dto, user.getUsername(), reactionId);
    return CustomResponse.success(reaction);
  }

  @Operation(summary = "반응 제거", description = "기존 반응을 제거합니다.")
  @DeleteMapping("/{reactionId}")
  public ResponseEntity<?> removeReaction(
      @PathVariable Long reactionId,
      @AuthenticationPrincipal User user
  ) {
    reactionService.removeReaction(reactionId, user.getUsername());
    return CustomResponse.success();
  }

  @Operation(summary = "메시지의 반응 조회", description = "메시지 ID로 반응 목록을 조회합니다.")
  @GetMapping("/message/{messageId}")
  public ResponseEntity<?> getReactionsByMessageId(@PathVariable Long messageId) {
    List<ReactionDto> reactions = reactionService.getReactionsByMessageId(messageId);
    return CustomResponse.success(reactions);
  }
}
