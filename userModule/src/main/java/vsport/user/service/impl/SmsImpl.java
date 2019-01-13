package vsport.user.service.impl;

import com.montnets.mwgate.smsutil.SmsSendConn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsport.SmsContentUtil;
import vsport.SmsSdk;
import vsport.entity.SmsContent;
import vsport.user.domain.SmsCodeEntity;
import vsport.user.enums.ResultEnum;
import vsport.user.enums.SmsError;
import vsport.user.redis.SmsCodeRedis;
import vsport.user.redis.impl.SmsCodeRedisImpl;
import vsport.user.service.SmsService;
import vsport.user.util.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 实现验证码逻辑：
 * 同一个手机号一天只能收取4条验证码
 * 同一个手机号发送验证码最小间隔为5分钟
 * 一个验证码15分钟内验证为有效
 * 验证码存活24小时
 */
@Service
public class SmsImpl implements SmsService {

    @Autowired
    private SmsCodeRedis mSmsCodeRedis;

    @Autowired
    private SmsSdk smsSdk;

    @Autowired
    private TimeUtil mTimeUtil;

    private List<SmsCodeEntity> mSmsCodeEntityList;

    @Override
    public int sendSmsCode(String mobile, int smsType) {
        synchronized (mobile) {
            //todo 错误处理后续还要重构的,需要做日志不然都不知道发生了什么
            if (isDayLimit(mobile)) {
                return 0;
            }

            if (!isIntervalOk(mobile)) {
                return 0;
            }

            return send(mobile, smsType);
        }
    }

    @Override
    public int verifySmsCode(String mobile, String smsCode) {
        //验证码有效期为15分钟
        SmsCodeEntity smsCodeEntity = mSmsCodeRedis.getSmsCode(mobile);
        long currentTime = System.currentTimeMillis();

        if (smsCodeEntity == null) {
            return ResultEnum.PHONE_NOT_REGISTER.getCode();
        }

        long intervalTime = currentTime - smsCodeEntity.sendTime;
        int validMilliSec = 15 * 60 * 1000;
        boolean isInvalid = mTimeUtil.compareTime(intervalTime, validMilliSec);
        //开始验证
        if (isInvalid || smsCodeEntity.isVeriry
                || !smsCode.equals(smsCodeEntity.smsCode)) {
            return ResultEnum.SMS_INVAILD_CODE.getCode();
        }

        //修改状态
        mSmsCodeRedis.updateSmsCode(mobile, smsCodeEntity.sendTime, SmsCodeRedisImpl.HasVeriry);

        return 0;
    }


//    @Override
//    public void getOldSmsCodes(String mobile) {
//        mSmsCodeEntityList =
//                mSmsCodeRedis.getOldSmsCodes(mobile, Constant.SMS_DAY_COUNT);
//    }

    private int send(String mobile, int smsType) {
        //发送验证码
        SmsContent smsContent = SmsContentUtil.getContent(smsType);
        if (smsContent == null) {
            return ResultEnum.SMS_TYPE_ERROR.getCode();
        }

        SmsSendConn smsSendConn = smsSdk.getSmsSendConn();
        int result = smsSdk.sigleSend(smsSendConn, mobile, smsContent.getContent());
        //todo 错误处理耦合度太高
        if (result == SmsError.MOBILE_ERROR.getCode()) {
            return ResultEnum.SMS_MOBILE_ERROR.getCode();
        } else if (result < 0) {
            return ResultEnum.SMS_SEND_ERROR.getCode();
        }

        mSmsCodeRedis.saveSmsCode(mobile, smsContent.getCode());

        return 0;
    }

    protected boolean isDayLimit(String mobile) {
        mSmsCodeEntityList =
                mSmsCodeRedis.getOldSmsCodes(mobile, SmsCodeRedisImpl.SMS_DAY_COUNT);

        if (mSmsCodeEntityList.size() < SmsCodeRedisImpl.SMS_DAY_COUNT) {
            return false;
        }

        SmsCodeEntity compareCode =
                mSmsCodeEntityList.get(SmsCodeRedisImpl.SMS_DAY_COUNT - 1);

        long dayBeginTime = mTimeUtil.getDayBeginTime(new Date());
        return mTimeUtil.compareTime(compareCode.sendTime, dayBeginTime);
    }

    //两次验证码时间间隔必须大于3分钟
    protected boolean isIntervalOk(String mobile) {
        SmsCodeEntity smsCodeEntity = mSmsCodeRedis.getSmsCode(mobile);
        if (smsCodeEntity == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();

        long intervalTime = currentTime - smsCodeEntity.sendTime;
        int reSendInterval = 3 * 60 * 1000;
        return mTimeUtil.compareTime(intervalTime, reSendInterval);
    }

//    @Override
//    public int sendSmsCode(String mobile, String smsType) {
//        //发送验证码
//        SmsContent smsContent = mSmsContentUtil.getContent(smsType);
//        if (smsContent == null) {
//            return ResultEnum.SMS_TYPE_ERROR.getCode();
//        }
//
//        SmsSendConn smsSendConn = smsSdk.getSmsSendConn();
//        int result = smsSdk.sigleSend(smsSendConn, mobile, smsContent.getContent());
//        if (result == SmsError.MOBILE_ERROR.getCode()) {
//            return ResultEnum.SMS_MOBILE_ERROR.getCode();
//        } else if (result < 0) {
//            return ResultEnum.SMS_SEND_ERROR.getCode();
//        }
//
//        //保存验证码
//        mSmsCodeRedis.saveSmsCode(mobile, smsContent.getCode());
//        return 0;
//    }


}
