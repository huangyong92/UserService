package vsport.user.redis.impl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vsport.user.domain.User;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRedisImplTest {

    @Autowired
    private UserRedisImpl userRedis;

    @Test
    public void test1_saveUser() {
        User user = new User();
        user.setUserId("123");
        user.setNickName("小龙");
        user.setGender(0);
        user.setPassword("abc");
        user.setPhone("1234567");
        user.setAvatar("http://abc");

        userRedis.saveUser(user);
    }

    @Test
    public void test4_updateUser() {
        User user = new User();
        user.setUserId("1234567");
        user.setNickName("小虎");
        user.setLivingCity("hz");
        user.setSportTarget("lose");

        userRedis.updateUser(user);
    }

    @Test
    public void test2_findUserById() {
        User user = userRedis.findUserById("123");

        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void test3_findUserByPhone() {
        User user = userRedis.findUserByPhone("1234567");

        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void test5_findIdByName() {
        Set<String> oldIdSet = userRedis.findIdByName("小龙");
        Set<String> idSet = userRedis.findIdByName("小虎");

        boolean isOldExit = oldIdSet.contains("123");
        boolean isExit = idSet.contains("123");
        assertEquals(false, isOldExit);
        assertEquals(true, isExit);
    }

    @Test
    public void test5_findIdBycity() {
        Set<String> idSet = userRedis.findIdByCity("hz");

        boolean isExit = idSet.contains("123");
        assertEquals(true, isExit);
    }

    @Test
    public void test5_findIdByTarget() {
        Set<String> idSet = userRedis.findIdByTarget("lose");

        boolean isExit = idSet.contains("123");
        assertEquals(true, isExit);
    }
}