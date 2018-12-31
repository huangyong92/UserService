package vsport.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsport.user.domain.User;
import vsport.user.mq.UserRepository;
import vsport.user.redis.UserRedis;
import vsport.user.service.UserService;
import vsport.user.util.KeyUtil;

import java.io.IOException;
import java.util.List;

@Service
public class UserImpl implements UserService {

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private UserRedis userRedis;

    @Override
    public String addUser(User user) {
        String userId = KeyUtil.getUserId();
        user.setUserId(userId);

        userRedis.saveUser(user);
        mUserRepository.addUser(user);

        return userId;
    }

    @Override
    public User findUserByPhone(String phone) throws IOException {
        User user = userRedis.findUserByPhone(phone);

        if (user == null) {
            user = mUserRepository.findUserByPhone(phone);
        }
        return user;
    }

    @Override
    public List<User> findUserByName(String name) throws IOException {
        List<User> userList = userRedis.findUserByName(name);

        if (userList == null || userList.size() == 0) {
            //todo 如果要求高的话，需要定期刷新数据库到缓存
            userList = mUserRepository.findUserByName(name);
        }

        return userList;
    }

    @Override
    public User findUserById(String uid) throws IOException {
        User user = userRedis.findUserById(uid);

        if (user == null) {
            user = mUserRepository.findUserById(Integer.parseInt(uid));
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        mUserRepository.updateUser(user);
        //用户持久化到redis,所以不用等数据库成功之后再更新
        userRedis.updateUser(user);
    }

//    @Override
//    public boolean modifyPassword(String phone, String password) {
//        User user = mUserRepository.findUserByPhone(phone);
//        if (user != null) {
//            mUserRepository.updatePassword(user.getUserId(), password);
//
//            return true;
//        }
//
//        return false;
//    }
}
