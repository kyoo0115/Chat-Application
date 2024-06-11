package project.realtimechatapplication.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageListener {

  @KafkaListener(topics = "chat_messages", groupId = "group_id")
  public void listen(String message) {
    log.info("Received message: {}", message);
  }
}
