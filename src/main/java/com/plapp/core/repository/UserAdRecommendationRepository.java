package com.plapp.core.repository;

import com.plapp.core.entity.UserAdRecommendationEntity;
import com.plapp.core.repository.custom.UserAdRecommendationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdRecommendationRepository extends JpaRepository<UserAdRecommendationEntity, Long>, UserAdRecommendationRepositoryCustom {
}