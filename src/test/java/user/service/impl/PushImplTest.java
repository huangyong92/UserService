package user.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushImplTest {

    @Autowired
    private PushImpl push;

    private String clientId = "1df8559716e00195db2b1a0eeba283ce";
    private String yClientId = "afa4b62008f1376205717e3578587f93";


    @Test
    public void notifLoginOut() {
        push.notifLoginOut(clientId);
    }

    @Test
    public void inviteClass() {
        List<String> appIds = new ArrayList<>();
        appIds.add(clientId);
        appIds.add(yClientId);

        push.inviteClass("567", appIds);
    }
}