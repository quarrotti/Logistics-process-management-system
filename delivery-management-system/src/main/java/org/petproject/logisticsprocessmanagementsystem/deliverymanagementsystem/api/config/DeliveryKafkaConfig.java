package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.config;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.petproject.logisticsprocessmanagementsystem.kafkacore.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeliveryKafkaConfig {
    private final String bootstrapServers = "localhost:9092,localhost:9094";

    @Bean
    NewTopic createTopicDoCreated(){
        return TopicBuilder.name("do-order-created-event-topic")
                .partitions(2)
                .replicas(2)
                .build();
    }
    @Bean
    NewTopic createTopicDoComplete(){
        return TopicBuilder.name("do-order-complete-event-topic")
                .partitions(2)
                .replicas(2)
                .build();
    }

    @Bean
    NewTopic createTopicDiFail(){
        return TopicBuilder.name("do-order-fail-created-event-topic")
                .partitions(2)
                .replicas(2)
                .build();
    }



    @Bean
    public ProducerFactory<String, OrderEvent> deliveryProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderEvent> orderKafkaTemplate() {
        return new KafkaTemplate<>(deliveryProducerFactory());
    }


    @Bean
    public ConsumerFactory<String, OrderEvent> consumerFactory() {//todo
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "delivery-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }
}
