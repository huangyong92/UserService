package vsport.user.service.impl;

import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsport.user.domain.BatchObject;
import vsport.user.service.PushService;
import vsport.user.util.PushUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushImpl implements PushService {

    @Autowired
    private PushUtil mPushUtil;

    @Override
    public void notifLoginOut(String appId) {
        TransmissionTemplate template = mPushUtil.transmissionTemplateProduce("退出登录", PushUtil.WAIT_START_APP);
        mPushUtil.siglePush(template, appId);
    }

    @Override
    public void inviteClass(String classId, List<String> appIds) {
        TransmissionTemplate template = mPushUtil.transmissionTemplateProduce("课程id为" + classId, PushUtil.WAIT_START_APP);

        List<BatchObject> batchObjects = new ArrayList<>();

        for (String appId :
                appIds) {
            BatchObject batchObject = new BatchObject(template, appId);
            batchObjects.add(batchObject);

            mPushUtil.batchPush(batchObjects);
        }
    }
}
