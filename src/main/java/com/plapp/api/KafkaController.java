package com.plapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plapp.kafka.code.AdManagerTaskType;
import com.plapp.kafka.code.TopicTypeCode;
import com.plapp.kafka.dto.AdMessageDto;
import com.plapp.kafka.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/click")
    public String sendMessage() throws JsonProcessingException {
        AdMessageDto adMessageDto = AdMessageDto
                .builder()
                .taskType(AdManagerTaskType.AD_CLICK.getCode())
                .userId(1L)
                .adId(1L)
                .build();
        kafkaProducer.sendMessage(TopicTypeCode.AD_MANAGER, adMessageDto);
        return "Message sent to Kafka: ";
    }
}
