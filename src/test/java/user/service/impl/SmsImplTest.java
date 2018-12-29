package user.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import user.enums.SmsType;
import user.redis.impl.SmsCodeRedisImpl;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsImplTest {

    @Autowired
    private SmsImpl mSmsService;

    @Autowired
    private SmsCodeRedisImpl smsCodeRedis;

    private String mobile = "13575732183";

    private String smsCode = "555";

    @Test
    public void testSmsCode() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(300);

        String abc = "abc";
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (abc) {
                    boolean isDayLimit = mSmsService.isDayLimit(mobile);

                    if (isDayLimit) {
                        System.out.println("1");
                        countDownLatch.countDown();
                        return;
                    }

                    boolean isIntervalOk = mSmsService.isIntervalOk(mobile);

                    if (!isIntervalOk) {
                        System.out.println("3");
                        countDownLatch.countDown();
                        return;
                    }

                    smsCodeRedis.saveSmsCode(mobile, smsCode);
                    countDownLatch.countDown();
                }
            }
        };


        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(500);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 300, 10, TimeUnit.SECONDS,
                blockingQueue);

        for (int i = 0; i < 300; i++) {
            threadPoolExecutor.execute(runnable);
        }

        countDownLatch.await();
        threadPoolExecutor.shutdown();
    }

    @Test
    public void sendCode() {
        mSmsService.sendSmsCode(mobile, SmsType.LOGIN.getType());
    }

    @Test
    public void verifyCode() {
        int statu = mSmsService.verifySmsCode(mobile, "1437");
        assertEquals(0, statu);
    }
}