package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.entity.MissionEntity;
import com.kariqu.tyt.common.persistence.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {

    @Autowired
    protected MissionRepository missionRepository;

    public MissionRepository getMissionRepository() {
        return missionRepository;
    }

}
