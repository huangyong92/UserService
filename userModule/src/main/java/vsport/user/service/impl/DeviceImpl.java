package vsport.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsport.user.redis.DeviceRedis;
import vsport.user.service.DeviceService;

@Service
public class DeviceImpl implements DeviceService {

    @Autowired
    private DeviceRedis mDeviceRedis;

    @Override
    public void loginDevice(String userId, String deviceId) {
        mDeviceRedis.saveLoginDevice(userId, deviceId);
    }

    @Override
    public String getCurrentDevice(String userId) {
        return mDeviceRedis.getCurrentDevice(userId);
    }
//
//    @Override
//    public Set<String> getDevices(String userId) {
//        return mDeviceRedis.getDeviceIds(userId);
//    }
}
