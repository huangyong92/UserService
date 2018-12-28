package user.enums;

import lombok.Getter;

@Getter
public enum SmsType {
    LOGIN(0),
    FIND_PASSWORD(1);

    int type;

    SmsType(int type) {
        this.type = type;
    }
}
