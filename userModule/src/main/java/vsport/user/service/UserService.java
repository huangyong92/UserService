package vsport.user.service;


import vsport.user.domain.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    String addUser(User user);

    User findUserByPhone(String phone) throws IOException;

    List<User> findUserByName(String name) throws IOException;

    User findUserById(String uid) throws IOException;

    void updateUser(User user);
}
