package net.caidingke.rabbitmq.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MessageConsumerService implements MessageListener {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private AmqpAdmin amqpAdmin;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public void onMessage(Message message) {
		log.info(String.format("接收到消息：%s", message.toString()));
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonObject jsonObject = objectMapper.readValue(message.getBody(), JsonObject.class);
			log.info(jsonObject.toString());
			log.info(String.format(String.format("jsonObject name : == %s", jsonObject.getName())));
			log.info(String.format("jsonObject age : == %s", jsonObject.getAge()));
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
