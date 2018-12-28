package user.redis.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsCodeRedisTest {

    @Autowired
    private SmsCodeRedis smsCodeRedis;

    private String mobile = "13575732183";
    private String smsCode = "555";

    @Test
    public void saveSmsCode() {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(30);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    int randInt = random.nextInt(10000);

                    smsCodeRedis.saveSmsCode(mobile, String.valueOf(randInt));
                    System.out.println(randInt);
                    countDownLatch.countDown();
                }
            };

            BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 30, 60, TimeUnit.SECONDS, blockingQueue);
            for (int i = 0; i < 30; i++) {
                threadPoolExecutor.execute(runnable);
            }

            countDownLatch.await();
            threadPoolExecutor.shutdown();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSmsCode() {
        SmsCodeEntity smsCodeEntity = smsCodeRedis.getSmsCode(mobile);

        String smsCode = "";
        if (smsCodeEntity != null) {
            smsCode = smsCodeEntity.getSmsCode();
        }

        assertNotEquals("", smsCode);
    }

    @Test
    public void getSmsCodes() {
        List<SmsCodeEntity> smsCodeEntities = smsCodeRedis.getOldSmsCodes(mobile, 4);

        assertNotNull(smsCodeEntities);
        assertNotEquals(0, smsCodeEntities.size());
    }

    @Test
    public void updateSmsCode() {
        SmsCodeEntity smsCodeEntity = smsCodeRedis.getSmsCode(mobile);
        long sendTime = smsCodeEntity.getSendTime();

        smsCodeRedis.updateSmsCode(mobile, sendTime, SmsCodeRedis.HasVeriry);

        SmsCodeEntity newSmsCode = smsCodeRedis.getSmsCode(mobile);
        boolean isVeriry = newSmsCode.isVeriry();

        assertEquals(true, isVeriry);
    }
}