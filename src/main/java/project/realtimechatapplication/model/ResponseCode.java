package project.realtimechatapplication.model;

public interface ResponseCode {

  String SUCCESS = "SU";
  String VALIDATION_FAIL = "VF";
  String DUPLICATE_ID = "DI";

  String SIGN_IN_FAIL = "SF";
  String VERIFICATION_FAIL = "VF";

  String MAIL_FAIL = "MF";
  String DATABASE_ERROR = "DBE";
}
