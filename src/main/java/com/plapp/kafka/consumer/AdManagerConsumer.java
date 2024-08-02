package com.plapp.kafka.consumer;

import com.plapp.kafka.code.AdManagerTaskType;
import com.plapp.kafka.dto.AdMessageDto;
import com.plapp.kafka.error.ErrorHandler;
import com.plapp.kafka.service.AdManagerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Event에 따라 메시지를 처리하는 Consumer를 만들고 싶을 때 참고하시면 좋습니다.
 */
@Service
@Slf4j
public class AdManagerConsumer {
    private final ErrorHandler errorHandler;
    private final AdManagerService adManagerService;

    public AdManagerConsumer(ErrorHandler errorHandler, AdManagerService adManagerService) {
        this.errorHandler = errorHandler;
        this.adManagerService = adManagerService;
    }

    @KafkaListener(topics = "ad-manager", groupId = "ad-manager-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment){
        String message = record.value();
        try {
            AdMessageDto adMessage = new ObjectMapper().readValue(message, AdMessageDto.class);
            AdManagerTaskType taskType = AdManagerTaskType.fromCode(adMessage.getTaskType());
            switch (taskType) {
                case AD_VIEW:
                    adManagerService.recordViewEvent(adMessage);
                    break;
                case AD_CLICK:
                    adManagerService.recordClickEvent(adMessage);
                    break;
                case CALL_SIMILAR_ADS_LIST:
                    adManagerService.callSimilarAdsList(adMessage);
                    break;
                case RECOMMENDED_AD:
                    adManagerService.saveRecommendedAd(adMessage);
                    break;
                default:
                    log.warn("Unknown task type received: {}", taskType);
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Consumer Failed to process message: {}", e.getMessage());
            errorHandler.handleProcessingError(record, acknowledgment, 1);
        }
    }
}