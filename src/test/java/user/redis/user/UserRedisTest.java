package user.redis.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import user.domain.User;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRedisTest {

    @Autowired
    private UserRedis userRedis;

    @Test
    public void saveUser() {
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
    public void updateUser() {
        User user = new User();
        user.setUserId("123");
        user.setNickName("小虎");
        user.setLivingCity("hz");
        user.setSportTarget("lose");

        userRedis.updateUser(user);
    }

    @Test
    public void findUserById() {
        User user = userRedis.findUserById("123");

        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void findUserByPhone() {
        User user = userRedis.findUserByPhone("1234567");

        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void findIdByName() {
        Set<String> oldIdSet = userRedis.findIdByName("小龙");
        Set<String> idSet = userRedis.findIdByName("小虎");

        boolean isOldExit = oldIdSet.contains("123");
        boolean isExit = idSet.contains("123");
        assertEquals(false, isOldExit);
        assertEquals(true, isExit);
    }

    @Test
    public void findIdBycity() {
        Set<String> idSet = userRedis.findIdByCity("hz");

        boolean isExit = idSet.contains("123");
        assertEquals(true, isExit);
    }

    @Test
    public void findIdByTarget() {
        Set<String> idSet = userRedis.findIdByTarget("lose");

        boolean isExit = idSet.contains("123");
        assertEquals(true, isExit);
    }
}