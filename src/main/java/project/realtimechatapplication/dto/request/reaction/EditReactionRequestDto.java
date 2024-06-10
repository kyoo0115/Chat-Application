package project.realtimechatapplication.dto.request.reaction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.model.type.Reaction;

@Getter
@Setter
@NoArgsConstructor
public class EditReactionRequestDto {

    @NotNull(message = "Reaction ID cannot be null")
    private Long reactionId;

    @NotNull(message = "Reaction type cannot be null")
    private Reaction reaction;
}
