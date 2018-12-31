package vsport.user.service;

public interface SmsService {

    int sendSmsCode(String mobile, int smsType);

    int verifySmsCode(String mobile, String smsCode);
}
