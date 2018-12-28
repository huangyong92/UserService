package user.service;

import java.util.Set;

public interface DeviceService {

    void loginDevice(String userId, String deviceId);

    String getCurrentDevice(String userId);
}
