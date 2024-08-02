package com.plapp.core.repository;

import com.plapp.core.entity.AdEntity;
import com.plapp.core.repository.custom.AdRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<AdEntity, Long>, AdRepositoryCustom {
}