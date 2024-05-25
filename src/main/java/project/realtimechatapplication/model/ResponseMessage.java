package project.realtimechatapplication.model;

public interface ResponseMessage {

  String SUCCESS = "Success.";
  String VALIDATION_FAIL = "Validation failed.";
  String DUPLICATE_ID = "Duplicate Id.";

  String SIGN_IN_FAIL = "Login information mismatch.";
  String VERIFICATION_FAIL = "Verification failed.";

  String MAIL_FAIL = "Mail sending failed.";
  String DATABASE_ERROR = "Database error.";
}
