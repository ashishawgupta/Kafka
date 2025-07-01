package com.learnkafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;


@Configuration
@Slf4j
// In Older version @EnableKafka Annotation is required but not in the latest version
//@EnableKafka
public class LibraryEventConsumerConfig {

//    private final KafkaProperties properties;
//
//    public LibraryEventConsumerConfig(KafkaProperties properties) {
//        this.properties = properties;
//    }
//
//    public DefaultErrorHandler errorHandler() {
//
//        var fixedBackOff = new FixedBackOff(1000L, 2L);
//
//        var defaultErrorHandler = new DefaultErrorHandler(
//                fixedBackOff
//        );
//
//        errorHandler().setRetryListeners(((record, ex, deliveryAttempt) -> {
//            log.info("Failed Recrod in Retry Listener, Exception : {}, deliveryAttempt: {} ", ex.getMessage(), deliveryAttempt);
//        }));
//
//        return defaultErrorHandler;
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
//            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
//            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory,
//            ObjectProvider<ContainerCustomizer<Object, Object, ConcurrentMessageListenerContainer<Object, Object>>> kafkaContainerCustomizer) {
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        configurer.configure(factory, kafkaConsumerFactory
//                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties())));
//        //factory.setConcurrency(3);
//        //factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        factory.setCommonErrorHandler(errorHandler());
//        return factory;
//    }
}
