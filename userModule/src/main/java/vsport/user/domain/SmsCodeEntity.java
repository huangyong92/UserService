package vsport.user.domain;

import lombok.Data;

@Data
public class SmsCodeEntity {

    public String mobile;

    public String smsCode;

    public long sendTime;

    public boolean isVeriry;

    public SmsCodeEntity() {
    }

    public SmsCodeEntity(String mobile, String smsCode, long sendTime, boolean isVeriry) {
        this.mobile = mobile;
        this.smsCode = smsCode;
        this.sendTime = sendTime;
        this.isVeriry = isVeriry;
    }
}
