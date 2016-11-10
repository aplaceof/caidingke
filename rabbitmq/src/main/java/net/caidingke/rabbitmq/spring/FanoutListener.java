package net.caidingke.rabbitmq.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author bowen
 * @create 2016-11-03 10:49
 */
@Service
public class FanoutListener implements MessageListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onMessage(Message message) {
        System.out.println("接收到消息：" + message.toString());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonObject jsonObject = objectMapper.readValue(message.getBody(), JsonObject.class);
            System.out.println(jsonObject.toString());
            System.out.println("jsonObject name : ==" + jsonObject.getName());
            System.out.println("jsonObject age : ==" + jsonObject.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
