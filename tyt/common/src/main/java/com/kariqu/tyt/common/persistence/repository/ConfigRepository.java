package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, Integer> {
}
