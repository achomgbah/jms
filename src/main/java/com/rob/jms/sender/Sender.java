package com.rob.jms.sender;

import com.rob.jms.config.JmsConfig;
import com.rob.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class Sender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("Hello kompis").build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
        log.info("Message sent");
    }
}
