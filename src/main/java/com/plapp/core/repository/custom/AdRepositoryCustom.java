package com.plapp.core.repository.custom;

import com.plapp.core.entity.AdEntity;
import com.plapp.kafka.code.AdType;

import java.util.List;

public interface AdRepositoryCustom {
    List<AdEntity> findSimilarTypeAdByAdType(AdType adType);
}
