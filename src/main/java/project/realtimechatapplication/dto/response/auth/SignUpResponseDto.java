//package project.realtimechatapplication.dto.response.auth;
//
//import static org.springframework.http.HttpStatus.BAD_REQUEST;
//import static org.springframework.http.HttpStatus.OK;
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//import static project.realtimechatapplication.model.ResponseMessage.DUPLICATE_ID;
//import static project.realtimechatapplication.model.ResponseMessage.VERIFICATION_FAIL;
//
//import org.springframework.http.ResponseEntity;
//
//public class SignUpResponseDto extends ResponseDto {
//
//  private SignUpResponseDto() {
//    super();
//  }
//
//  public static ResponseEntity<SignUpResponseDto> success() {
//    SignUpResponseDto responseBody = new SignUpResponseDto();
//    return ResponseEntity.status(OK).body(responseBody);
//  }
//
//  public static ResponseEntity<ResponseDto> duplicateId() {
//    ResponseDto responseBody = new ResponseDto(DUPLICATE_ID, DUPLICATE_ID);
//    return ResponseEntity.status(BAD_REQUEST).body(responseBody);
//  }
//
//  public static ResponseEntity<ResponseDto> verificationFail() {
//    ResponseDto responseBody = new ResponseDto(VERIFICATION_FAIL, VERIFICATION_FAIL);
//    return ResponseEntity.status(UNAUTHORIZED).body(responseBody);
//  }
//}
