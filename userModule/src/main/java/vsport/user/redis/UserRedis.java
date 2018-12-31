package vsport.user.redis;


import vsport.user.domain.User;

import java.util.List;

public interface UserRedis {

    void saveUser(User user);

    void updateUser(User user);

    User findUserById(String userId);

    User findUserByPhone(String phone);

    List<User> findUserByName(String name);

    List<User> findUserByCity(String city);

    List<User> findUserByTarget(String target);
}
