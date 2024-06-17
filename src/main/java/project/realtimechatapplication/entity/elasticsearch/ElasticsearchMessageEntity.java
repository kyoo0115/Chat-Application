package project.realtimechatapplication.entity.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import project.realtimechatapplication.entity.MessageEntity;
import project.realtimechatapplication.entity.TimeStamped;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "chat_messages")
public class ElasticsearchMessageEntity extends TimeStamped {

  @Id
  private Long id;
  @Field(type = FieldType.Text)
  private String type;
  @Field(type = FieldType.Text)
  private String message;
  @Field(type = FieldType.Long)
  private Long chatRoomId;
  @Field(type = FieldType.Text)
  private String sender;

  public static ElasticsearchMessageEntity from(MessageEntity messageEntity) {
    return ElasticsearchMessageEntity.builder()
        .id(messageEntity.getId())
        .type(messageEntity.getType().toString())
        .message(messageEntity.getMessage())
        .chatRoomId(messageEntity.getChatRoom().getId())
        .sender(messageEntity.getUser().getUsername())
        .build();
  }
}
