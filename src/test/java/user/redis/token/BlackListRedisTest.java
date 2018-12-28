package user.redis.token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlackListRedisTest {

    @Autowired
    private BlackListRedis blackListRedis;

    @Test
    public void addToBlackList() {
        blackListRedis.addToBlackList("abc");
    }

    @Test
    public void isAtBlcakList() {
        boolean isAtList = blackListRedis.isAtBlcakList("abc");

        assertEquals(true, isAtList);
    }
}