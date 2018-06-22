package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer> {
}
