package project.realtimechatapplication.handler;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import project.realtimechatapplication.provider.TokenProvider;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

  private final TokenProvider tokenProvider;
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String AUTHORIZATION_HEADER = "Authorization";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (StompCommand.CONNECT == accessor.getCommand()) {
      String jwtToken = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
      if (jwtToken != null && jwtToken.startsWith(BEARER_PREFIX)) {
        String token = jwtToken.substring(BEARER_PREFIX.length());
        if (tokenProvider.validateToken(token)) {
          accessor.setUser(() -> tokenProvider.getUsernameFromToken(token));
        } else {
          throw new IllegalArgumentException("Invalid token");
        }
      }
    }
    return message;
  }
}
