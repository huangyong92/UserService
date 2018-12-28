package user.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenImplTest {

    @Autowired
    private TokenImpl tokenService;

    String userId = "123";
    String token = "eyJhbGciOiJQQkVTMi1IUzI1NitBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwicDJjIjo4MTkyLCJwMnMiOiJidXU4NFpVS2QxTXVsRmlWIn0.HtSC8swzv2sEqjUNqe-dwNM4dKvloAL8cdU1yACGm0xYxx9_bktjmw.pjUmzlEVPARGlXOM1cXISw.g3McziO1m98CNg5nq1_mhQ.67gC9Q-Bs3-4_q3wYD9Ivw";

    @Test
    public void createToken() {
        String token123 = tokenService.createToken(userId);
        System.out.println(token123);
    }

    @Test
    public void parseToken() {
        String userId = tokenService.parseToken(token);

        assertEquals(this.userId, userId);
    }

    @Test
    public void addToBlackList() {
        tokenService.addToBlackList(token);
    }

    @Test
    public void isAtBlackList() {
        boolean isBlack = tokenService.isAtBlackList(token);
        assertEquals(true, isBlack);
    }
}