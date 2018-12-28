package user.service.impl;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.redis.token.BlackListRedis;
import user.service.TokenService;
import user.util.TokenUtil;

@Service
public class TokenImpl implements TokenService {

    @Autowired
    private TokenUtil mTokenUtil;

    @Autowired
    private BlackListRedis mBlackListRedis;

    @Override
    public String createToken(String userId) {
        try {
            return mTokenUtil.createToken(userId);

            //todo 日志记录和常见异常处理
        } catch (JoseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String parseToken(String token) {
        try {
            return mTokenUtil.parseToken(token);
        } catch (JoseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addToBlackList(String token) {
        mBlackListRedis.addToBlackList(token);
    }

    @Override
    public boolean isAtBlackList(String token) {
        return mBlackListRedis.isAtBlcakList(token);
    }
}
