package com.plapp.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_daily_ad_statistics")
public class DailyAdStatisticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_ad_statistics_id")
    private Long dailyAdStatisticsId;

    @Column(name = "ad_id", nullable = false, length = 255)
    private Long adId;

    @Column(name = "stat_date", nullable = false)
    private LocalDateTime statDate;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "clicks", nullable = false)
    private Integer clicks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
