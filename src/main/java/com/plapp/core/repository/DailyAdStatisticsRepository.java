package com.plapp.core.repository;

import com.plapp.core.entity.DailyAdStatisticsEntity;
import com.plapp.core.repository.custom.DailyAdStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyAdStatisticsRepository extends JpaRepository<DailyAdStatisticsEntity, Long>, DailyAdStatisticsRepositoryCustom {
}