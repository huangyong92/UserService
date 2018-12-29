package user.redis;

import user.domain.User;

import java.util.List;
import java.util.Set;

public interface UserRedis {

    void saveUser(User user);

    void updateUser(User user);

    User findUserById(String userId);

    User findUserByPhone(String phone);

    List<User> findUserByName(String name);

    List<User> findUserByCity(String city);

    List<User> findUserByTarget(String target);
}
