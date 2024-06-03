package project.realtimechatapplication.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MessageSendRequestDto {

  @NotBlank
  private String senderId;
  @NotBlank
  private String receiverId;
  @NotBlank
  private String message;
}
