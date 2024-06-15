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
import project.realtimechatapplication.entity.ChatRoomEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "chat_rooms")
public class ElasticsearchChatRoomEntity {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String name;

  @Field(type = FieldType.Boolean)
  private boolean isPrivate;

  @Field(type = FieldType.Text)
  private String roomCode;

  @Field(type = FieldType.Text)
  private String owner;

  public static ElasticsearchChatRoomEntity from(ChatRoomEntity chatRoomEntity) {
    return ElasticsearchChatRoomEntity.builder()
        .id(chatRoomEntity.getId())
        .name(chatRoomEntity.getName())
        .isPrivate(chatRoomEntity.isPrivate())
        .roomCode(chatRoomEntity.getRoomCode())
        .owner(chatRoomEntity.getOwner())
        .build();
  }
}
