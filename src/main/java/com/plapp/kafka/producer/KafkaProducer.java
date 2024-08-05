package com.plapp.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plapp.kafka.code.TopicTypeCode;
import com.plapp.kafka.dto.AdMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Configuration
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaProducerListener kafkaProducerListener) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate.setProducerListener(kafkaProducerListener);
    }

    public void sendMessage(TopicTypeCode topicTypeCode, AdMessageDto adMessageDto) throws JsonProcessingException {
        // 유저가 클릭한 것과 유사한 광고 목록 조회
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topicTypeCode.getCode(),
                new ObjectMapper().writeValueAsString(adMessageDto)
        );
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        responseLogging(future);
    }

    public void sendMessage(ConsumerRecord<String, String> record){
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record.topic(), record.key(), record.value());
        responseLogging(future);
    }

    private void responseLogging(CompletableFuture<SendResult<String, String>> future) {
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Success: {}", result);
            }
            else {
                log.error("Failure Result: {}, ExceptionMessage: {}", result, ex.getMessage());
            }
        });
    }
}
