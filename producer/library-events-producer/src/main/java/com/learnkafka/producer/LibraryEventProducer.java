package com.learnkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventProducer {

    @Value("${spring.kafka.topic}")
    public String topic;

    private KafkaTemplate<Integer, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public LibraryEventProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper)   {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Approach 1
    // 1. Blocking call - get metadata about the kafka cluster
    // 2. Send message happens - Returns a CompletableFuture
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEventAsync(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var completableFuture =  kafkaTemplate.send(topic, key, value);
        return completableFuture.whenComplete((sendResult, throwable) -> {
            if(throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        });
    }

    // Approach 2
    // 1. Blocking call - get metadata about the kafka cluster
    // 2. Block and wait until the message is sent to the Kafka
    public SendResult<Integer, String> sendLibraryEventsync(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var sendResult =  kafkaTemplate.send(topic, key, value).get();
        handleSuccess(key, value, sendResult);
        return sendResult;

    }

    // Approach 3
    // 1. Blocking call - get metadata about the kafka cluster
    // 2. Send message happens - Returns a CompletableFuture
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEventApproach3(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var producerRecord = buildProducerRecord(key, value);
        var completableFuture =  kafkaTemplate.send(producerRecord);
        return completableFuture.whenComplete((sendResult, throwable) -> {
            if(throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        });
    }

    // Approach 4: Sending KafkaRecord with Headers
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEventApproach4(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var producerRecord = buildProducerRecordWithHeaders(key, value);
        var completableFuture =  kafkaTemplate.send(producerRecord);
        return completableFuture.whenComplete((sendResult, throwable) -> {
            if(throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecordWithHeaders(Integer key, String value) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {
        return new ProducerRecord<>(topic, key, value);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message Sent Successfully for the Key : {} and the value : {}, partition is {} ",
                key,value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable throwable) {
        log.error("Error sending the message and the exception is {}", throwable.getMessage(), throwable);
    }
}
