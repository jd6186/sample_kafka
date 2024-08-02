package com.plapp.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plapp.kafka.code.AdScheduledDataTransferTaskType;
import com.plapp.kafka.dto.AdMessageDto;
import com.plapp.kafka.error.ErrorHandler;
import com.plapp.kafka.service.AdScheduledDataTransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * Batch 성으로 메시지를 처리하는 Consumer를 만들고 싶을 때 참고하시면 좋습니다.
 */
@Service
@Slf4j
public class AdScheduledDataTransferConsumer {
    private final ErrorHandler errorHandler;
    private final AdScheduledDataTransferService adScheduledDataTransferService;

    public AdScheduledDataTransferConsumer(ErrorHandler errorHandler, AdScheduledDataTransferService adScheduledDataTransferService) {
        this.errorHandler = errorHandler;
        this.adScheduledDataTransferService = adScheduledDataTransferService;
    }

    @KafkaListener(topics = "ad-scheduled-data-transfer", groupId = "ad-transfer-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String message = record.value();
        try {
            AdMessageDto adMessageDto = new ObjectMapper().readValue(message, AdMessageDto.class);
            AdScheduledDataTransferTaskType taskType = AdScheduledDataTransferTaskType.fromCode(adMessageDto.getTaskType());
            switch (taskType) {
                case AD_SCHEDULED:
                    adScheduledDataTransferService.adScheduled(adMessageDto);
                    break;
                case AD_DATA_TRANSFER:
                    adScheduledDataTransferService.adDataTransfer(adMessageDto);
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
