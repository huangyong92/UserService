package vsport.user.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MqDeclare {

    public static final String USER_EXCHANGE = "userExchange";
    public static final String WRITE_USER_KEY = "saveUser";
    public static final String READ_USER_KEY = "getUser";

    @Bean
    public List<Declarable> userDeclare() {
        Queue saveQueue = new Queue("saveUserQue", true, false, false);
        Queue getQueue = new Queue("getUserQue", true, false, false);

        DirectExchange userEx = new DirectExchange("userExchange", true, false);
        return Arrays.asList(saveQueue, getQueue, userEx,
                BindingBuilder.bind(saveQueue).to(userEx).with("saveUser"),
                BindingBuilder.bind(getQueue).to(userEx).with("getUser"));
    }

}
