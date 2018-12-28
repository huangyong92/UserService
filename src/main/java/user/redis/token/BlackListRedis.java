package user.redis.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BlackListRedis {
    private static final String BLACK_KEY = "blcak_key";

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    public void addToBlackList(String token) {
        setOperations.add(BLACK_KEY, token);
    }

    public boolean isAtBlcakList(String token) {
        return setOperations.isMember(BLACK_KEY, token);
    }
}
