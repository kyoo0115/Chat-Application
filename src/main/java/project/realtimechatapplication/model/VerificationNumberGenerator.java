package project.realtimechatapplication.model;

public class VerificationNumberGenerator {

  public static String generateVerificationNumber() {

    StringBuilder verificationNumber = new StringBuilder();

    for (int i = 0; i < 4; i++) {
      verificationNumber.append((int) (Math.random() * 10));
    }
    return verificationNumber.toString();
  }
}
