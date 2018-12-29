package user.redis;

import user.domain.SmsCodeEntity;

import java.util.List;

public interface SmsCodeRedis {
    void saveSmsCode(String mobile, String smsCode);

    SmsCodeEntity getSmsCode(String mobile);

    List<SmsCodeEntity> getOldSmsCodes(String mobile, int count);

    void updateSmsCode(String mobile, long time, String isVeriry);
}
