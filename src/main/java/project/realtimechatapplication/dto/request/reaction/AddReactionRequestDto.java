package project.realtimechatapplication.dto.request.reaction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.model.type.Reaction;

@Getter
@Setter
@NoArgsConstructor
public class AddReactionRequestDto {

    @NotNull(message = "Message ID cannot be null")
    private Long messageId;

    @NotNull(message = "Reaction type cannot be null")
    private Reaction reaction;
}
