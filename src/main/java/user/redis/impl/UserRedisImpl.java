package user.redis.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.stereotype.Component;
import user.domain.User;
import user.redis.UserRedis;
import user.util.BeanUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class UserRedisImpl implements UserRedis {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, byte[], byte[]> hashOperations;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;

    public void saveUser(User user) {
        String phone = user.getPhone();
        String nickName = user.getNickName();
        String id = user.getUserId();
        String city = user.getLivingCity();
        String target = user.getSportTarget();

        String userKey = getUserIdHashKey(id);
        ObjectHashMapper objectHashMapper = new ObjectHashMapper();
        Map<byte[], byte[]> map = objectHashMapper.toHash(user);

        if (!redisTemplate.hasKey(userKey)) {
            hashOperations.putAll(userKey, map);
        }

        String phoneKey = getPhoneStrKey(phone);
        valueOperations.set(phoneKey, id);

        String nickNameKey = getNickNameListKey(nickName);
        setOperations.add(nickNameKey, id);

        if (city != null) {
            String cityKey = getCityListKey(city);
            setOperations.add(cityKey, id);
        }

        if (target != null) {
            String targetKey = getTargetListKey(target);
            setOperations.add(targetKey, id);
        }
    }

    public void updateUser(User user) {
        ObjectHashMapper objectHashMapper = new ObjectHashMapper();

        String id = user.getUserId();
        String userKey = getUserIdHashKey(id);

        Map<byte[], byte[]> oldMap = hashOperations.entries(userKey);
        User oldUser = objectHashMapper.fromHash(oldMap, User.class);
        //删除原先的属性
        deleteUserIndex(oldUser);

        //保存新的属性
        BeanUtils.copyProperties(user, oldUser, BeanUtil.getNullPropertyNames(user));
        saveUser(oldUser);
    }

    public User findUserById(String userId) {
        String userKey = getUserIdHashKey(userId);
        Map<byte[], byte[]> map = hashOperations.entries(userKey);

        //null测试
        ObjectHashMapper objectHashMapper = new ObjectHashMapper();
        return objectHashMapper.fromHash(map, User.class);
    }

    public User findUserByPhone(String phone) {
        String phoneKey = getPhoneStrKey(phone);
        String userkey = valueOperations.get(phoneKey);

        return findUserById(userkey);
    }

    public List<User> findUserByName(String name) {
        Set<String> set = findIdByName(name);

        //todo看看是否有别的办法组织数据，不要遍历
        List<User> userList = new ArrayList<>();
        for (String userKey :
                set) {
            User user = findUserById(userKey);

            userList.add(user);
        }

        return userList;
    }

    public List<User> findUserByCity(String city) {
        Set<String> set = findIdByCity(city);

        List<User> userList = new ArrayList<>();
        for (String userKey :
                set) {
            User user = findUserById(userKey);

            userList.add(user);
        }

        return userList;
    }

    public List<User> findUserByTarget(String target) {
        Set<String> set = findIdByTarget(target);

        List<User> userList = new ArrayList<>();
        for (String userKey :
                set) {
            User user = findUserById(userKey);

            userList.add(user);
        }

        return userList;
    }

    public Set<String> findIdByCity(String city) {
        String cityKey = getCityListKey(city);
        return setOperations.members(cityKey);
    }

    protected Set<String> findIdByName(String name) {
        String nameKey = getNickNameListKey(name);
        return setOperations.members(nameKey);
    }

    public Set<String> findIdByTarget(String target) {
        String targetKey = getTargetListKey(target);
        return setOperations.members(targetKey);
    }

    //删除用户的索引，但是不删除用户
    private void deleteUserIndex(User user) {
        String id = user.getUserId();
        String phone = user.getPhone();
        String nickName = user.getNickName();
        String city = user.getLivingCity();
        String target = user.getSportTarget();

        String phoneKey = getPhoneStrKey(phone);
        redisTemplate.delete(phoneKey);

        String nickNameKey = getNickNameListKey(nickName);
        setOperations.remove(nickNameKey, id);

        String cityKey = getCityListKey(city);
        setOperations.remove(cityKey, id);

        String targetKey = getTargetListKey(target);
        setOperations.remove(targetKey, id);
    }

    private String getPhoneStrKey(String phone) {
        return "phone_" + phone;
    }

    private String getNickNameListKey(String nickName) {
        return "nickname_" + nickName;
    }

    private String getUserIdHashKey(String userId) {
        return "userId_" + userId;
    }

    private String getCityListKey(String city) {
        return "city_" + city;
    }

    private String getTargetListKey(String target) {
        return "target_" + target;
    }
}
