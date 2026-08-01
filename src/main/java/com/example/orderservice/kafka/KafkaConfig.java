package com.example.orderservice.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.orderservice.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class KafkaConfig {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

    @Bean
    NewTopic orderTopic() {
        return TopicBuilder
                .name("orders-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    ProducerFactory<String, OrderEvent> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
        	    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        	    bootstrapServers);
        
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);

    }

    @Bean
    KafkaTemplate<String, OrderEvent> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());

    }
    @Bean
    public ConsumerFactory<String, OrderEvent> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
        	    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        	    bootstrapServers);

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "order-group");

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        config.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "com.example.orderservice.dto");

        config.put(
                JsonDeserializer.VALUE_DEFAULT_TYPE,
                "com.example.orderservice.dto.OrderEvent");

        return new DefaultKafkaConsumerFactory<>(config);

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;

    }

}