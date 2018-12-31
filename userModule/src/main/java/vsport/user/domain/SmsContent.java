package vsport.user.domain;

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
