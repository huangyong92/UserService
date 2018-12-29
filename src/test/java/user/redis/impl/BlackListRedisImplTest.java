package user.redis.impl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringRunner;
import user.SpringTest;
import user.redis.impl.BlackListRedisImpl;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlackListRedisImplTest extends SpringTest {

    @Autowired
    private BlackListRedisImpl blackListRedis;

    @Parameterized.Parameters
    public static Iterable<? extends Object> initData() {
        Random random = new Random();
        return Arrays.asList(
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(10000))
        );
    }

    @Parameterized.Parameter
    public String token;

    @Test
    public void TestA_addToBlackList() {
        System.out.println("token: " + token);
        blackListRedis.addToBlackList(token);
    }

    @Test
    public void TestB_isAtBlcakList() {
        boolean isAtList = blackListRedis.isAtBlcakList(token);

        assertEquals(true, isAtList);
    }
}