package user.redis.device;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import user.util.KeyUtil;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceRedisTest {
    @Autowired
    private DeviceRedis deviceRedis;

//    private static String uid = KeyUtil.getUserId();
    @Test
    public void saveLoginDevice() {
        deviceRedis.saveLoginDevice("123", "abc123ui");
    }

    @Test
    public void getCurrentDevice() {
        String deviceId = deviceRedis.getCurrentDevice("123");

        assertEquals("abc123ui", deviceId);
    }
}