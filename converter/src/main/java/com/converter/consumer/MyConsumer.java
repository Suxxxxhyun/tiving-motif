package com.converter.consumer;

import com.converter.model.MyMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer {

    @KafkaListener(
            topics = { "streaming-topic" },
            groupId = "test-consumer-group",
            properties = {
                    "spring.json.value.default.type:com.converter.model.MyMessage",
                    "spring.json.use.type.headers:false"
            }
    )
    public void accept(MyMessage message){
        System.out.println("Message arrived! = " + message);
    }
}
