package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.GameRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecordEntity, Long> {
}
