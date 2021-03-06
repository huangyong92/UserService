package vsport.user.redis.impl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vsport.user.SpringTest;
import vsport.user.util.KeyUtil;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceRedisImplTest extends SpringTest {
    @Autowired
    private DeviceRedisImpl deviceRedis;

    @Parameterized.Parameters
    public static Object[] initData() {
        Random random = new Random();
        return new Object[][]{{KeyUtil.getUserId(), String.valueOf(random.nextInt(10000))},
                {KeyUtil.getUserId(), String.valueOf(random.nextInt(10000))},
                {KeyUtil.getUserId(), String.valueOf(random.nextInt(10000))}};
    }

    @Parameterized.Parameter
    public String mUserId;

    @Parameterized.Parameter(1)
    public String mDeviceId;

    @Test
    public void testA_saveLoginDevice() {
        deviceRedis.saveLoginDevice(mUserId, mDeviceId);
    }

    @Test
    public void testB_getCurrentDevice() {
        String deviceId = deviceRedis.getCurrentDevice(mUserId);

        assertEquals(mDeviceId, deviceId);
    }
}