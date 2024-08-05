package com.plapp.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plapp.core.entity.DailyAdStatisticsEntity;
import com.plapp.core.repository.DailyAdStatisticsRepository;
import com.plapp.kafka.code.EventType;
import com.plapp.kafka.code.TopicTypeCode;
import com.plapp.kafka.dto.AdMessageDto;
import com.plapp.kafka.producer.KafkaProducer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AdScheduledDataTransferService {
    private final RedisTemplate<String, String> redisTemplate;
    private final DailyAdStatisticsRepository dailyAdStatisticsRepository;
    private final KafkaProducer kafkaProducer;

    public AdScheduledDataTransferService(RedisTemplate<String, String> redisTemplate, DailyAdStatisticsRepository dailyAdStatisticsRepository, KafkaProducer kafkaProducer) {
        this.redisTemplate = redisTemplate;
        this.dailyAdStatisticsRepository = dailyAdStatisticsRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public void adScheduled(AdMessageDto adMessageDto) throws JsonProcessingException {
        // 어제 저장된 광고 고유번호 목록 조회
        LocalDateTime searchDateTime = LocalDate.now().atStartOfDay().minusDays(1);
        adMessageDto.setDatetime(searchDateTime);
        Set<String> adIdSet = getAdIdsForSearchDateTime(searchDateTime);

        // 광고고유번호 데이터를 순회하며 메시지 전송 전송
        for (String adId : adIdSet) {
            sendScheduledDataTransferMessage(adMessageDto, Long.parseLong(adId));
        }
    }

    // 검색일에 저장된 광고 목록을 중복 제거해서 조회
    private Set<String> getAdIdsForSearchDateTime(LocalDateTime searchDateTime) {
        String date = searchDateTime.toLocalDate().toString();
        String key = String.format("eventType:*:adID:*:date:%s:hour:*", date);
        Set<String> itemSet = redisTemplate.opsForSet().members(key);
        if (itemSet == null) {
            return Set.of();
        }

        // adId 목록만 추출
        Pattern pattern = Pattern.compile("eventType:.*:adID:(.*):date:.*:hour:.*");
        return itemSet.stream()
                .map(item -> {
                    Matcher matcher = pattern.matcher(item);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // 시간대별로 메시지 전송
    private void sendScheduledDataTransferMessage(AdMessageDto adMessageDto, Long adId) throws JsonProcessingException {
        for (int hour=0; hour < 24; hour++) {
            adMessageDto.setAdId(adId);
            adMessageDto.setHour(String.format("%02d", hour));
            kafkaProducer.sendMessage(TopicTypeCode.AD_SCHEDULED_DATA_TRANSFER, adMessageDto);
        }
    }

    // 각 광고 시간대별로 데이터 조회 후 저장
    public void adDataTransfer(AdMessageDto adMessageDto) {
        // getDatetime의 시간을 getHour:00:00으로 변경해 시간대별로 데이터 저장
        LocalDateTime statDate = adMessageDto.getDatetime().withHour(Integer.parseInt(adMessageDto.getHour())).withMinute(0).withSecond(0);
        DailyAdStatisticsEntity dailyAdStatisticsEntity = DailyAdStatisticsEntity.builder()
                .adId(adMessageDto.getAdId())
                .statDate(statDate)
                .views(getRowCount(adMessageDto, EventType.CLICK))
                .clicks(getRowCount(adMessageDto, EventType.VIEW))
                .build();
        dailyAdStatisticsRepository.save(dailyAdStatisticsEntity);
    }

    // 광고 데이터 조회
    private int getRowCount(AdMessageDto adMessageDto, EventType eventType) {
        String date = adMessageDto.getDatetime().toLocalDate().toString();
        String key = String.format("eventType:%s:adID:%d:date:%s:hour:%s", eventType.name(), adMessageDto.getAdId(), date, adMessageDto.getHour());
        List<String> itemList = redisTemplate.opsForList().range(key, 0, -1);
        return itemList != null ? itemList.size() : 0;
    }
}
