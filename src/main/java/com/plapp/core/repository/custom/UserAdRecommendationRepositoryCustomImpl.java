package com.plapp.core.repository.custom;

import com.plapp.core.entity.QUserAdRecommendationEntity;
import com.plapp.core.entity.UserAdRecommendationEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class UserAdRecommendationRepositoryCustomImpl implements UserAdRecommendationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserAdRecommendationRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<UserAdRecommendationEntity> findByUserIdAndAdId(long userId, long adId) {
        QUserAdRecommendationEntity userAdRecommendationEntity = QUserAdRecommendationEntity.userAdRecommendationEntity;
        return Optional.ofNullable(queryFactory
                .selectFrom(userAdRecommendationEntity)
                .where(userAdRecommendationEntity.userId.eq(userId)
                        .and(userAdRecommendationEntity.adId.eq(adId)))
                .fetchFirst());
    }
}
