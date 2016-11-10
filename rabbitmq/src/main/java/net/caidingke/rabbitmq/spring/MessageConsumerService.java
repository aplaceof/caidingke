package net.caidingke.rabbitmq.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageConsumerService implements MessageListener {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private AmqpAdmin amqpAdmin;

	private static final ObjectMapper objectMapper = new ObjectMapper();

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
