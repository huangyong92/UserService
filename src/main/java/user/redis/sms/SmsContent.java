package user.redis.sms;

import lombok.Data;

@Data
public class SmsContent {

    private String content;

    private String code;

    public SmsContent(String code, String content) {
        this.content = content;
        this.code = code;
    }
}
