package vsport;

import com.montnets.mwgate.smsutil.SmsSendConn;

public interface SmsSdk {

    SmsSendConn getSmsSendConn();

    Integer sigleSend(SmsSendConn smsSendConn, String mobile,
                      String content);
}
