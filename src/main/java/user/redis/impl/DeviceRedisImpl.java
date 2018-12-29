package user.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import user.redis.DeviceRedis;
import user.service.DeviceService;

import javax.annotation.Resource;

/**
 * 存储用户设备，及当前设备
 * userId appId loginstatu
 */
@Component
public class DeviceRedisImpl implements DeviceRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    private String getLoginDeviceStrKey(String userId) {
        return "user:" + userId + "login_device";
    }

    private String getUserDeviceSetKey(String userId) {
        return "user:" + userId + "all_device";
    }

    public void saveLoginDevice(String userId, String deviceId) {
        String loginDeviceKey = getLoginDeviceStrKey(userId);
        valueOperations.set(loginDeviceKey, deviceId);

        addDeviceToAll(userId, deviceId);
    }

    public String getCurrentDevice(String userId) {
        String loginDeviceKey = getLoginDeviceStrKey(userId);
        return valueOperations.get(loginDeviceKey);
    }

    private void addDeviceToAll(String userId, String deviceId) {
        String userDevicesKey = getUserDeviceSetKey(userId);
        setOperations.add(userDevicesKey, deviceId);
    }
}
