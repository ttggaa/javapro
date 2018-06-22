package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.CoinsLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CoinsLogRepository extends JpaRepository<CoinsLogEntity, Integer> {
}
