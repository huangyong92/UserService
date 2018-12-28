package user.util;

import com.montnets.mwgate.common.GlobalParams;
import com.montnets.mwgate.common.Message;
import com.montnets.mwgate.smsutil.ConfigManager;
import com.montnets.mwgate.smsutil.SmsSendConn;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "sms")
@Setter
public class SmsUtil {

    private SmsSendConn mSmsSendConn;

    public String account;

    public String password;

    @PostConstruct
    public void initSms() {
        configSms();
        setAccount(account, password);
    }

    public void configSms() {
        // 创建全局参数
        GlobalParams globalParams = new GlobalParams();
        // 设置请求路径
        globalParams.setRequestPath("/sms/v2/std/");
        // 设置是否需要日志 1:需要日志;0:不需要日志
        globalParams.setNeedLog(1);
        // 设置全局参数
        ConfigManager.setGlobalParams(globalParams);

        // 是否保持长连接
        boolean isKeepAlive = true;
        // 实例化短信处理对象
        mSmsSendConn = new SmsSendConn(isKeepAlive);
    }

    public void setAccount(String userid, String password) {
        // 发送优先级
        int priority = 1;
        // 主IP信息
        String ipAddress1 = "api01.monyun.cn:7901";

        // 备用IP1信息
        String ipAddress2 = "api02.monyun.cn:7901";
        // 备用IP2信息
        String ipAddress3 = null;
        // 备用IP3信息
        String ipAddress4 = null;
        // 返回值
        int result = -310007;
        try {
            // 设置用户账号信息
            result = ConfigManager.setAccountInfo(userid, password, priority,
                    ipAddress1, ipAddress2, ipAddress3, ipAddress4);
            // 判断返回结果，0设置成功，否则失败
            if (result == 0) {
                System.out.println("设置用户账号信息成功！");
            } else {
                System.out.println("设置用户账号信息失败，错误码：" + result);
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
    }

    public void removeAccount(String userid) {
        try {
            // 调用移除用户账号的方法
            int result = ConfigManager.removeAccount(userid);

            // 返回0，代表移除成功
            if (result == 0) {
                System.out.println("移除用户账号[" + userid + "]成功！");
            }
            // 返回非0，代表移除失败
            else {
                System.out.println("移除用户账号[" + userid + "]失败！错误码：" + result);
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
    }

    //todo 假如长连接断开怎么办
    public SmsSendConn getSmsSendConn() {
        if (mSmsSendConn == null) {//这样写应该不会有多线程问题了吧
            boolean isKeepAlive = true;
            // 实例化短信处理对象
            mSmsSendConn = new SmsSendConn(isKeepAlive);
        }

        return mSmsSendConn;
    }

    public Integer sigleSend(SmsSendConn smsSendConn, String mobile,
                             String content) {
        int result = -310099;
        try {
            Message message = new Message();
            message.setMobile(mobile);
            message.setContent(content);

            StringBuffer returnValue = new StringBuffer();
            result = smsSendConn.singleSend(message, returnValue);
            //todo 错误需要再这里处理
            //...test
            if (result == 0) {
                System.out.println("单条发送提交成功！");
                System.out.println(returnValue.toString());
            }
            // result为非0：失败
            else {
                System.out.println("单条发送提交失败,错误码：" + result);
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}
