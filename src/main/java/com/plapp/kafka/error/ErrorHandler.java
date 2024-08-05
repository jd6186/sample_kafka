package com.plapp.kafka.error;

import com.plapp.kafka.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorHandler {
    private final KafkaProducer kafkaProducer;

    public ErrorHandler(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    private static final String RETRY_COUNT_HEADER = "retry-count"; // 현재 재시도 횟수가 적힌 헤더 이름

    public void handleProcessingError(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, int maxRetryCount) {
        // header에서 retry-count 조회하여 검증 진행
        try {
            // 현재 재시도 횟수 조회
            int retryCount = record.headers().lastHeader(RETRY_COUNT_HEADER) != null
                    ? Integer.parseInt(new String(record.headers().lastHeader(RETRY_COUNT_HEADER).value()))
                    : 0;
            if (retryCount < maxRetryCount) {
                // 최대 재시도 횟수를 초과하지 않은 경우 재시도 메시지 전송
                record.headers().add(RETRY_COUNT_HEADER, Integer.toString(retryCount + 1).getBytes());
                kafkaProducer.sendMessage(record);
                acknowledgment.acknowledge();
            } else {
                // 최대 재시도 횟수를 초과한 경우 로그 남기고 메시지 소비
                log.error("Message failed after {} retries, forcibly consuming the message: {}", maxRetryCount, record.value());
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            // 재시도 중 오류 발생 시 로그 남기고 강제로 메시지 소비
            log.error("Failed to handle processing error: {}", e.getMessage());
            acknowledgment.acknowledge();
        }
    }
}
