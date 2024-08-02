package com.plapp.core.repository.custom;

import com.plapp.core.entity.AdEntity;
import com.plapp.core.entity.QAdEntity;
import com.plapp.kafka.code.AdType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class AdRepositoryCustomImpl implements AdRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AdRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<AdEntity> findSimilarTypeAdByAdType(AdType adType) {
        QAdEntity adEntity = QAdEntity.adEntity;
        return queryFactory
                .selectFrom(adEntity)
                .where(adEntity.adType.eq(adType))
                .fetch();
    }
}
