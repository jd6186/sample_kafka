package com.plapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plapp.api.dto.AdClickRequestDto;
import com.plapp.kafka.code.AdManagerTaskType;
import com.plapp.kafka.code.TopicTypeCode;
import com.plapp.kafka.dto.AdMessageDto;
import com.plapp.kafka.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/ad")
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    // TODO - 원하는 컨트롤러를 작성해주세요, 필요 시 별로 서비스도 제작해주세요 ^^
    @PostMapping("/click")
    public String sendMessage(@RequestBody AdClickRequestDto adClickRequestDto) throws JsonProcessingException {
        AdMessageDto adMessageDto = AdMessageDto
                .builder()
                .taskType(AdManagerTaskType.AD_CLICK.getCode())
                .userId(adClickRequestDto.getUserId())
                .adId(adClickRequestDto.getAdId())
                .build();
        kafkaProducer.sendMessage(TopicTypeCode.AD_MANAGER, adMessageDto);
        return "Message sent User: " + adMessageDto.getUserId() + ", clicked ad " + adMessageDto.getAdId();
    }
}
