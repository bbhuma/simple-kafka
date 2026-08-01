package com.example.orderservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.orderservice.dto.OrderEvent;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "orders-topic",
            groupId = "order-group")
    public void consume(OrderEvent event) {

        System.out.println();

        System.out.println("===================================");

        System.out.println("Order Received");

        System.out.println("Id       : " + event.getId());

        System.out.println("Product  : " + event.getProduct());

        System.out.println("Quantity : " + event.getQuantity());

        System.out.println("===================================");

    }

}