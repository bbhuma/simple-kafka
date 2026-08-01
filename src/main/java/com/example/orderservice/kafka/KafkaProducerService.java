package com.example.orderservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.orderservice.dto.OrderEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderEvent event) {

        kafkaTemplate.send("orders-topic", event);

        System.out.println("Published : " + event.getId());

    }

}