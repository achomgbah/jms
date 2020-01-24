package com.rob.jms.listener;

import com.rob.jms.config.JmsConfig;
import com.rob.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class Listener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message) {
        log.info("Message recieved:{}", helloWorldMessage);
    }

    @JmsListener(destination = JmsConfig.MY_SEND_RECEIVE_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage payload = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("Hello paddy").build();

        log.info("Message recieved:{}", helloWorldMessage);
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payload);
    }
}
