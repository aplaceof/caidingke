package net.caidingke.rabbitmq.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bowen
 * @create 2017-01-11 18:26
 */
@Service
@Slf4j
public class FanoutListenerCustom {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public void onMessageHandler(JsonObject message) {
        log.info("接受到消息: {}", message.toString());
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //System.out.println(message.getBody());
        //try {
        //    JsonObject jsonObject = objectMapper.readValue(message.getBody(), JsonObject.class);
        //    log.info(jsonObject.toString());
        //    log.info("jsonObject name : == {}", jsonObject.getName());
        //    log.info("jsonObject age : == {}", jsonObject.getAge());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

}
