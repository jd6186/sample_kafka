package com.plapp.kafka.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdMessageDto {
    private String taskType;
    private Long userId;
    private Long adId;
    private LocalDateTime datetime;
    private String hour;
}