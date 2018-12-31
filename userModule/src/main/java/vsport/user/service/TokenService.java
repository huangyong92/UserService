package vsport.user.service;

public interface TokenService {

    String createToken(String userId);

    String parseToken(String token);

    void addToBlackList(String token);

    boolean isAtBlackList(String token);
}
