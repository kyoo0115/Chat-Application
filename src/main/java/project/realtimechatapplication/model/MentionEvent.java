package project.realtimechatapplication.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentionEvent {

  private String roomCode;
  private String message;
  private String sender;
  private List<String> mentions;
}
