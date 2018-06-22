package com.kariqu.tyt.common.persistence.repository;

import com.kariqu.tyt.common.persistence.entity.UserMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserMissionRepository extends JpaRepository<UserMissionEntity, Integer> {

    public List<UserMissionEntity> findAllByUserId(int userId);
}
