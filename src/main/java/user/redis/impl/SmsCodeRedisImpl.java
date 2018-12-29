package user.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import user.domain.SmsCodeEntity;
import user.redis.SmsCodeRedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SmsCodeRedisImpl implements SmsCodeRedis {

    public final static Integer SMS_DAY_COUNT = 4;

    public final static String HasVeriry = "1";

    public final static String NotVeriry = "0";

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;


    private String getMobileKey(String mobile) {
        return "mobile:" + mobile;
    }

    private String getSmsCodeKey(String mobile, String time) {
        return mobile + ":" + time;
    }

    //内部调用
    private SmsCodeEntity getSmsCode(String mobile, String sendTime) {
        String smsCodeKey = getSmsCodeKey(mobile, sendTime);
        String smsCode = hashOperations.get(smsCodeKey, "code");
        String isVerify = hashOperations.get(smsCodeKey, "isVerify");

        if (smsCode == null || isVerify == null) {
            return null;
        }

        return new SmsCodeEntity(mobile, smsCode,
                Long.valueOf(sendTime),
                (Integer.valueOf(isVerify) > 0 ? true : false)
        );
    }

    public void saveSmsCode(String mobile, String smsCode) {
        long nowTime = System.currentTimeMillis();

        String mobileKey = getMobileKey(mobile);
        String nowTimeStr = String.valueOf(nowTime);

        listOperations.leftPush(mobileKey, nowTimeStr);
        //由于list里面的值不能够设置过期，所以为了不累积过多的没用的验证码，可以手动删除
        if (listOperations.size(mobileKey) == SMS_DAY_COUNT + 1) {
            listOperations.rightPop(mobileKey);
        }

        String smsCodeKey = getSmsCodeKey(mobile, nowTimeStr);
        Map<String, String> smsValue = new HashMap<>();
        smsValue.put("code", smsCode);
        smsValue.put("isVerify", NotVeriry);

        hashOperations.putAll(smsCodeKey, smsValue);
        redisTemplate.expire(smsCodeKey, 24 * 3600, TimeUnit.SECONDS);
    }

    public SmsCodeEntity getSmsCode(String mobile) {
        String sendTime = listOperations.index(getMobileKey(mobile), 0);

        if (sendTime == null) {
            return null;
        }

        return getSmsCode(mobile, sendTime);
    }

    public List<SmsCodeEntity> getOldSmsCodes(String mobile, int count) {
        String mobileKey = getMobileKey(mobile);

        List<SmsCodeEntity> smsCodeList = new ArrayList<>();
        List<String> recentSmsTime =
                listOperations.range(mobileKey, 0, count - 1);
        for (String time :
                recentSmsTime) {
            SmsCodeEntity smsCodeEntity = getSmsCode(mobile, time);
            smsCodeList.add(smsCodeEntity);
        }

        return smsCodeList;
    }

    public void updateSmsCode(String mobile, long time, String isVeriry) {
        String smsCodeKey = getSmsCodeKey(mobile, String.valueOf(time));

        hashOperations.put(smsCodeKey, "isVerify", isVeriry);
    }
}
