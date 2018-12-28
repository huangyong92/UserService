package user.redis.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 存储用户设备，及当前设备
 * userId appId loginstatu
 */
@Component
public class DeviceRedis {

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

//        String userDevicesKey = getUserDeviceSetKey(userId);
//        valueOperations.set(userDevicesKey, deviceId);
    }

//    public Set<String> getDeviceIds(String userId) {
//        String deviceKey = getDeviceKey(userId);
//        return setOperations.members(deviceKey);
//    }

    public String getCurrentDevice(String userId) {
//        Set<String> deviceIds = getDeviceIds(userId);

//        String deviceKey = getDeviceKey(userId);
//        Set<String> deviceIds = setOperations.members(deviceKey);
//        for (String device :
//                deviceIds) {
//            String loginKey = getLoginStatuKey(userId, device);
//            String loginStatu = valueOperations.get(loginKey);
//
//            if (loginStatu.equals(LoginEnum.LOGIN.getStatu())) {
//                return device;
//            }
//        }

        String loginDeviceKey = getLoginDeviceStrKey(userId);
        return valueOperations.get(loginDeviceKey);
    }

    private void addDeviceToAll(String userId, String deviceId) {
        String userDevicesKey = getUserDeviceSetKey(userId);
        setOperations.add(userDevicesKey, deviceId);
    }

//    public boolean isExit(String userId, String deviceId) {
//        Jedis jedis = mJedisPool.getResource();
//
//        String deviceKey = getDeviceKey(userId);
//        return jedis.sismember(deviceKey, deviceId);
//    }
}
