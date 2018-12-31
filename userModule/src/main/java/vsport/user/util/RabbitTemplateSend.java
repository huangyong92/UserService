package vsport.user.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vsport.user.domain.MessageObject;


@Component
public class RabbitTemplateSend<T> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void convertAndSend(String exchange, String routingKey,
                               String methodType, T object) {
        MessageObject<T> messageObject = new MessageObject<>(methodType, object);

        rabbitTemplate.convertAndSend(exchange, routingKey, messageObject);
    }

    public String convertSendAndReceive(String exchange, String routingKey,
                                        String methodType, T object) {
        MessageObject<T> messageObject = new MessageObject<>(methodType, object);

        String obj = (String) rabbitTemplate.convertSendAndReceive(
                exchange,
                routingKey,
                messageObject);

        return obj;
    }
}
