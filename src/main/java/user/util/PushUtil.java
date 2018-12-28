package user.util;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import user.domain.BatchObject;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "push")
@Setter
public class PushUtil {

    public static final Integer AUTO_START_APP = 1;
    public static final Integer WAIT_START_APP = 2;

    public String appid;
    public String appkey;
    public String master_secret;
    public String url;

    private static IGtPush mGtPush;

    @PostConstruct
    public void createGtPush() {
        if (mGtPush == null) {
            mGtPush = new IGtPush(url, appkey, master_secret);
        }
    }

    //todo 后续可能可以改成工厂和建造者
    //单个用户推送或则单个别名推送
    public void siglePush(AbstractTemplate template,
                          String clientId) {
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);

        Target target = new Target();
        target.setAppId(appid);
        target.setClientId(clientId);
        IPushResult ret = null;
        try {
            ret = mGtPush.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = mGtPush.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }

    //对列表用户推送(单个用户或则少量用户用单推最好)
    public void listPush(AbstractTemplate template,
                         List<Target> targetList) {
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);

        // taskId用于在推送时去查找对应的message
        String taskId = mGtPush.getContentId(message);
        IPushResult ret = mGtPush.pushMessageToList(taskId, targetList);
    }

    //对指定应用推送(手机型号，地理位置，操作系统等)
    //todo

    // 批量单推(用于一次创建提交多个单推),内容和推送对象都是不同的
    public void batchPush(List<BatchObject> objectList) {
        IBatch batch = mGtPush.getBatch();

        try {
            for (BatchObject batchObject : objectList) {
                SingleMessage message = new SingleMessage();
                message.setData(batchObject.template);
                message.setOffline(true);
                message.setOfflineExpireTime(24 * 3600 * 1000);

                Target target = new Target();
                target.setAppId(appid);
                target.setClientId(batchObject.clientId);

                batch.add(message, target);
                batch.submit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //透传消息
    public TransmissionTemplate transmissionTemplateProduce(String msg, Integer transmissionType) {
        TransmissionTemplate template = (TransmissionTemplate) createTemplate(new TransmissionTemplate());
        template.setTransmissionContent(msg);
        template.setTransmissionType(transmissionType); // 这个Type为int型，填写1则自动启动app

        return template;
    }

    //通知消息
    public LinkTemplate linkTemplateProduce(AbstractNotifyStyle style,
                                            String url) {
        LinkTemplate template = (LinkTemplate) createTemplate(new LinkTemplate());
        template.setStyle(style);

        if (url != null) {
            template.setUrl(url);
        }

        return template;
    }

    //通知透传
    public NotificationTemplate notificationTemplateProduct(AbstractNotifyStyle style,
                                                            String transmissionContent,
                                                            Integer transmissionType) {
        NotificationTemplate template = (NotificationTemplate) createTemplate(new NotificationTemplate());
        template.setStyle(style);

        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(transmissionType);
        template.setTransmissionContent(transmissionContent);

        return template;
    }

    //todo 是否可以弄成继承体系
    private AbstractTemplate createTemplate(AbstractTemplate template) {
        template.setAppId(appid);
        template.setAppkey(appkey);

        return template;
    }
}
