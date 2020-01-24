package com.rob.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rob.jms.config.JmsConfig;
import com.rob.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class Sender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("Hello kompis").build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
        log.info("Message sent");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReciveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("Hello buddy").build();

        Message recieved = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RECEIVE_QUEUE, session -> {
            log.info("sending hello");
            Message helloMessage = null;
            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            helloMessage.setStringProperty("_type", HelloWorldMessage.class.getCanonicalName());
            return helloMessage;
        });
        log.info("Message recieved:{}", recieved.getBody(String.class));
    }

}
