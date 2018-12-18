package user.mq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user.config.MqDeclare;
import user.domain.User;
import user.util.RabbitTemplateSend;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {

    @Autowired
    private RabbitTemplateSend rabbitTemplateSend;

    public void addUser(User user) {
        rabbitTemplateSend.convertAndSend(MqDeclare.USER_EXCHANGE,
                MqDeclare.WRITE_USER_KEY, "addUser", user);
    }

    public void updateUser(User user) {
        rabbitTemplateSend.convertAndSend(MqDeclare.USER_EXCHANGE,
                MqDeclare.WRITE_USER_KEY, "updateUser", user);
    }

    public void updatePassword(Integer userId,
                               String password) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        rabbitTemplateSend.convertAndSend(MqDeclare.USER_EXCHANGE,
                MqDeclare.WRITE_USER_KEY, "updatePassword", user);
    }

    public User findUserById(Integer userId) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserById", map);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(obj, User.class);
        return user;
    }

    public User findUserByPhone(String phone) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserByPhone", map);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(obj, User.class);

        return user;
    }

    public List<User> findUserByName(String name) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserByPhone", map);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference type = new TypeReference<List<User>>() {
        };

        return objectMapper.readValue(obj, type);
    }

    public List<User> findUserBycity(String city) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("city", city);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserBycity", map);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference type = new TypeReference<List<User>>() {
        };

        return objectMapper.readValue(obj, type);
    }

    public List<User> findUserByTarget(String target) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("target", target);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserByTarget", map);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference type = new TypeReference<List<User>>() {
        };

        return objectMapper.readValue(obj, type);
    }

    public String findUserByPhoneAndPassword(String phone,
                                             String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);

        String obj = rabbitTemplateSend.convertSendAndReceive(MqDeclare.USER_EXCHANGE,
                MqDeclare.READ_USER_KEY,
                "findUserByPhoneAndPassword", map);

        return obj;
    }
}
