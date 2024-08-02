package com.plapp.core.repository.custom;

import com.plapp.core.entity.UserAdRecommendationEntity;

import java.util.Optional;

public interface UserAdRecommendationRepositoryCustom {
    Optional<UserAdRecommendationEntity> findByUserIdAndAdId(long userId, long adId);
}
