package project.realtimechatapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.realtimechatapplication.model.ResponseCode;
import project.realtimechatapplication.model.ResponseMessage;

@Getter
@AllArgsConstructor
public class ResponseDto {

  private String code;
  private String message;

  public ResponseDto() {
    this.code = ResponseCode.SUCCESS;
    this.message = ResponseMessage.SUCCESS;
  }
}
