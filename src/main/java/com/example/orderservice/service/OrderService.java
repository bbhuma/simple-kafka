package com.example.orderservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.KafkaProducerService;
import com.example.orderservice.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository repository ;
	private final KafkaProducerService producer;
    public OrderService(OrderRepository repository,
            KafkaProducerService producer) {

this.repository = repository;
this.producer = producer;

}

    public Order save(OrderRequest request){
    	
    	Order order = new Order();
    	order.setProduct(request.getProduct());
    	order.setQuantity(request.getQuantity());
    	
    	Order saved = repository.save(order);

    	OrderEvent event = new OrderEvent();

    	event.setId(saved.getId());
    	event.setProduct(saved.getProduct());
    	event.setQuantity(saved.getQuantity());

    	producer.send(event);

    	return saved;
    }

    public List<Order> getAll(){

        return repository.findAll();

    }

}