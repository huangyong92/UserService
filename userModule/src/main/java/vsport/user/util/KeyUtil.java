package vsport.user.util;

import java.util.Random;
import java.util.UUID;

public class KeyUtil {

    public static String getLiveCourseKey(int publisherId, long startTime) {
        return String.valueOf(publisherId) + startTime;
    }

    public static String getSportThemeKey(int userId, long startTime) {
        return String.valueOf(userId) + startTime;
    }

    public static String getSportDetailKey() {
        Random random = new Random();
        Integer number = random.nextInt(9999) + 10000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

    public static String getUserId() {
        return UUID.randomUUID().toString();
    }
}
