package net.caidingke.spring.boot;

import net.caidingke.rabbitmq.spring.MessageProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author bowen
 * @create 2016-11-02 15:36
 */
@SpringBootApplication
@EnableScheduling
@ImportResource({"classpath*:application-*.xml"})
public class MessageInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MessageInitializer.class);
    }

}
