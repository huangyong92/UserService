package user.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import user.domain.User;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void addUser() {
        User user1 = new User();

        user1.setPhone("13575732183");
        user1.setNickName("Daniel");
        user1.setPassword("555");
        user1.setAvatar("http://xxx.png");
        user1.setGender(1);
        user1.setLivingCity("杭州");

        User user2 = new User();
        user2.setPhone("13588199148");
        user2.setNickName("小丫头");
        user2.setPassword("123");
        user2.setAvatar("http://xxx2.png");
        user2.setGender(0);
        user2.setLivingCity("杭州");

        User user3 = new User();
        user3.setPhone("13738766778");
        user3.setNickName("小勇");
        user3.setPassword("123");
        user3.setAvatar("http://xxx2.png");
        user3.setGender(1);
        user3.setLivingCity("杭州");

        User user4 = new User();
        user4.setPhone("1111111111");
        user4.setNickName("小太阳");
        user4.setPassword("123");
        user4.setAvatar("http://xxx2.png");
        user4.setGender(1);
        user4.setLivingCity("杭州");

        userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.addUser(user3);
        userRepository.addUser(user4);
    }

    @Test
    public void updateUser() {
        User user = new User();


        user.setUserId("1");
        user.setAvatar("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2885559027,2082458421&fm=27&gp=0.jpg");
        userRepository.updateUser(user);
    }

    @Test
    public void updatePassword() {
        userRepository.updatePassword("1", "1010");
    }

    @Test
    public void findUserById() throws IOException {
        User user = userRepository.findUserById(1);

        assertNotNull(user);
    }

    @Test
    public void findUserByPhone() throws IOException {
        User user = userRepository.findUserByPhone("13575732183");

        assertNotNull(user);
    }

    @Test
    public void findUserByName() throws IOException {
        List<User> userList = userRepository.findUserByName("n");

        assertNotEquals(0, userList);
    }

    @Test
    public void findUserBycity() throws IOException {
        List<User> userList = userRepository.findUserBycity("杭州");

        assertNotEquals(0, userList);
    }

    @Test
    public void findUserByTarget() throws IOException {
        List<User> userList = userRepository.findUserByTarget("");

        assertNotEquals(0, userList);
    }

    @Test
    public void getPassword() {
        String userId = userRepository.findUserByPhoneAndPassword("Daniel", "555");

        assertNotEquals(null, userId);
    }

    @Test
    public void findUserByPhoneAndPassword() {
        String userId = userRepository.findUserByPhoneAndPassword("13588199148"
                , "123");

        assertEquals(2, userId);
    }
}