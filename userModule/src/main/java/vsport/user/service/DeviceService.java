package vsport.user.service;

public interface DeviceService {

    void loginDevice(String userId, String deviceId);

    String getCurrentDevice(String userId);
}
