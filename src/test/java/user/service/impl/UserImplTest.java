package user.service.impl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import user.domain.User;
import user.service.UserService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserImplTest {

    @Autowired
    private UserImpl userService;

    @Test
    public void test1_addUser() {

        User user = new User();
        user.setUserId("1");
        user.setNickName("小明");
        user.setGender(0);
        user.setPassword("ming123");
        user.setPhone("13456789");
        user.setAvatar("www.xxx.com");
        userService.addUser(user);
    }

    @Test
    public void test2_updateUser() {
        User user = new User();
        user.setUserId("1");
        user.setPhone("199");

        userService.updateUser(user);
    }

    @Test
    public void test3_findUserByPhone() throws IOException {
        User user = userService.findUserByPhone("199");

        assertNotEquals(null, user);
    }

    @Test
    public void test3_findUserByName() throws IOException {
        List<User> user = userService.findUserByName("小明");

        assertNotEquals(0, user.size());
    }

    @Test
    public void test3_findUserById() throws IOException {
        User user = userService.findUserById("1");

        assertNotEquals(null, user);
    }
}