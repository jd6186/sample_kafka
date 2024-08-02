package com.plapp.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plapp.core.entity.AdEntity;
import com.plapp.core.entity.UserAdRecommendationEntity;
import com.plapp.core.repository.AdRepository;
import com.plapp.core.repository.UserAdRecommendationRepository;
import com.plapp.kafka.code.AdManagerTaskType;
import com.plapp.kafka.code.EventType;
import com.plapp.kafka.code.TopicTypeCode;
import com.plapp.kafka.dto.AdMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdManagerService {
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AdRepository adRepository;
    private final UserAdRecommendationRepository userAdRecommendationRepository;

    public AdManagerService(RedisTemplate<String, String> redisTemplate, KafkaTemplate<String, String> kafkaTemplate, AdRepository adRepository, UserAdRecommendationRepository userAdRecommendationRepository) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.adRepository = adRepository;
        this.userAdRecommendationRepository = userAdRecommendationRepository;
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH");

    public void recordViewEvent(AdMessageDto adMessageDto) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DATE_FORMATTER);
        String hour = now.format(HOUR_FORMATTER);
        String key = String.format("eventType:%s:adID:%d:date:%s:hour:%s", EventType.VIEW.name(), adMessageDto.getAdId(), date, hour);
        redisTemplate.opsForValue().increment(key, 1);
    }

    public void recordClickEvent(AdMessageDto adMessageDto) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DATE_FORMATTER);
        String hour = now.format(HOUR_FORMATTER);
        // 클릭 이벤트 Key 생성, eventType:{EventType}:adID:{adId}:date:{date}:hour:{hour}
        String key = String.format("eventType:%s:adID:%d:date:%s:hour:%s", EventType.CLICK.name(), adMessageDto.getAdId(), date, hour);
        redisTemplate.opsForValue().increment(key, 1);
        // 유저가 클릭한 것과 유사한 광고 목록 조회
        kafkaTemplate.send(TopicTypeCode.AD_MANAGER.getCode(), new ObjectMapper().writeValueAsString(
                        AdMessageDto
                                .builder()
                                .taskType(AdManagerTaskType.CALL_SIMILAR_ADS_LIST.getCode())
                                .userId(adMessageDto.getUserId())
                                .build()
                )
        );
    }

    public void callSimilarAdsList(AdMessageDto adMessageDto) throws JsonProcessingException {
        // 유사 광고 목록 조회
        AdEntity ad = adRepository.findById(adMessageDto.getAdId()).orElseThrow(
                () -> new IllegalArgumentException("Ad not found")
        );
        List<AdEntity> similarAds = adRepository.findSimilarTypeAdByAdType(ad.getAdType());
        adMessageDto.setTaskType(AdManagerTaskType.RECOMMENDED_AD.getCode());

        // 추천 광고 저장 메시지 호출
        for (AdEntity adEntity : similarAds) {
            adMessageDto.setAdId(adEntity.getAdId());
            kafkaTemplate.send(
                    TopicTypeCode.AD_MANAGER.getCode(),
                    new ObjectMapper().writeValueAsString(adMessageDto)
            );
        }
    }

    // 해당 광고 고유번호를 고객 추천 광고로 저장 로직 구현
    public void saveRecommendedAd(AdMessageDto adMessageDto) {
        UserAdRecommendationEntity userAdRecommendationEntity = userAdRecommendationRepository
                .findByUserIdAndAdId(adMessageDto.getUserId(), adMessageDto.getAdId())
                .orElse(null);
        if (userAdRecommendationEntity != null) {
            return;
        }
        userAdRecommendationRepository.save(UserAdRecommendationEntity
                .builder()
                .userId(adMessageDto.getUserId())
                .adId(adMessageDto.getAdId())
                .build()
        );
    }
}
