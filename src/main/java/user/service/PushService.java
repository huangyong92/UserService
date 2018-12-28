package user.service;

import java.util.List;

public interface PushService {

    void notifLoginOut(String appId);

    void inviteClass(String classId, List<String> appIds);
}
