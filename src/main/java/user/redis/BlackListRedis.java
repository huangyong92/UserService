package user.redis;

public interface BlackListRedis {

    void addToBlackList(String token);

    boolean isAtBlcakList(String token);
}
