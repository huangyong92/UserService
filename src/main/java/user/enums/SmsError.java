package user.enums;

import lombok.Getter;

@Getter
public enum SmsError {
    OK(0, "发送正常"),
    NOT_ENOUGH_BALANCE(-100003, "用户欠费"),
    MOBILE_ERROR(-100012, "手机号不合法"),
    SEND_SIGLE_ERROR(-310001, "单条发送失败"),
    HTTP_REQUEST_FAILED(-310099, "http请求失败")
    ;


    private int code;
    private String msg;

    SmsError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
