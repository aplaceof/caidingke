package net.caidingke.rabbitmq.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author bowen
 * @create 2016-11-03 11:14
 */
@Service
@Slf4j
public class TopicListener implements MessageListener{

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        log.info("接收到消息：{}", message.toString());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonObject jsonObject = objectMapper.readValue(message.getBody(), JsonObject.class);
            log.info(jsonObject.toString());
            log.info("jsonObject name : == {}", jsonObject.getName());
            log.info("jsonObject age : == {}", jsonObject.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
