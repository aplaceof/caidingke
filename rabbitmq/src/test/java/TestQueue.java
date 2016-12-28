import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.val;
import net.caidingke.rabbitmq.spring.JsonObject;
import net.caidingke.rabbitmq.spring.MessageProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bowen
 * @create 2016-11-02 11:03
 */

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-mq.xml"})
public class TestQueue {
    @Autowired
    private MessageProducerService producerService;

    @Test
    public void send() {
        val example = new ArrayList<>();

        //for (int i = 100 ; i >0 ; i--) {
        JsonObject object = new JsonObject();
        object.setName("bowen");
        object.setAge(25);

        //producerService.pushToMessageQueue("queue_fan",object);
        //producerService.pushToMessageQueue("rabbit_queue_two",object);
        //producerService.pushToMessage(object);
        //producerService.pushToMessageQueue("rabbit_queue_two", "Hello World");
        //}
        JsonObject name = new JsonObject();
        name.setAge(30);
        name.setName("challenge");

        //producerService.pushToMessage(name);
        producerService.sendFanoutMsg(object);

        //producerService.sendTopicMsg("zhu.p1",object);
        //producerService.sendTopicMsg("fddffs",object);
        //producerService.sendDataToCrQueue(object);
    }

}
