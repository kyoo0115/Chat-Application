package project.realtimechatapplication.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.realtimechatapplication.entity.UserEntity;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String username;
  private String name;
  private String email;

  public static UserDto from(UserEntity userEntity) {
    return UserDto.builder()
        .id(userEntity.getId())
        .username(userEntity.getUsername())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .build();
  }
}
