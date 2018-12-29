package user.redis;

public interface DeviceRedis {
    void saveLoginDevice(String userId, String deviceId);

    String getCurrentDevice(String userId);
}
