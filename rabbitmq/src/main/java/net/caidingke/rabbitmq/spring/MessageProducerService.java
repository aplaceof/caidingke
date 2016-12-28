package net.caidingke.rabbitmq.spring;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bowen
 * @create 2016-11-02 11:35
 */
@Service
public class MessageProducerService {

    //@Autowired
    //private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpTemplate fanoutTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate directTemplate;

    /**
     * convertAndSend：将Java对象转换为消息发送到匹配Key的交换机中Exchange。
     * 原文：Convert a Java object to an Amqp Message and send it to a default exchange with a specific routing key.
     * @param message
     */
    //public void pushToMessageQueue(Object message) {
    //    amqpTemplate.convertAndSend("rabbit_queue_one", message);
    //}

    //public void pushToMessage(Object message) {
    //    amqpTemplate.convertAndSend("rabbit_queue_two",message);
    //}


    /**
     * 点对点
     */
    public void sendDataToCrQueue(Object obj) {
        directTemplate.convertAndSend("queue_one_key", obj);
    }

    /**
     * 发送 发布--订阅消息
     */
    public void sendFanoutMsg(Object obj) {
        fanoutTemplate.convertAndSend(obj);
    }

    /**
     * 主题
     */
    public void sendTopicMsg(String topic, Object obj) {
        rabbitTemplate.convertAndSend("topic-exchange", topic, obj);
    }

}
