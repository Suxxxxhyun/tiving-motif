package com.major.domain.producer;

import com.major.domain.dto.MyMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyProducer {

    private final KafkaTemplate<String, MyMessage> kafkaTemplate;

    public void sendMessage(MyMessage message) {
        kafkaTemplate.send("streaming-topic", message);
    }
}
