package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.configs;

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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InventoryKafkaConfig {
    private final String bootstrapServers = "localhost:9092,localhost:9094";

    @Bean
    NewTopic createTopicIo(){
        return TopicBuilder.name("io-order-created-event-topic")
                .partitions(2)
                .replicas(2)
                .build();
    }

    @Bean
    NewTopic createTopicId(){
        return TopicBuilder.name("id-order-created-event-topic")
                .partitions(2)
                .replicas(2)
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderEvent> orderProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderEvent> orderKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }


    @Bean
    public ConsumerFactory<String, OrderEvent> consumerFactory() {//todo
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "response-banking");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }
}
