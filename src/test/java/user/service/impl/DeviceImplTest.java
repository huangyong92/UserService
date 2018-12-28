package user.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import user.service.DeviceService;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceImplTest {

    @Autowired
    private DeviceService device;

    @Test
    public void loginDevice() {
        device.loginDevice("abc123", "123");
        device.loginDevice("abc123", "456");
        device.loginDevice("abc123", "789");
    }

    @Test
    public void getCurrentDevice() {
        String deviceId = device.getCurrentDevice("abc123");
        System.out.println(deviceId);

        assertEquals("789", deviceId);
    }
}