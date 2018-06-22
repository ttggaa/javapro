package com.kariqu.tyt.common.persistence.service;

import com.kariqu.tyt.common.persistence.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Autowired
    protected ConfigRepository configRepository;

    public ConfigRepository getConfigRepository() {
        return configRepository;
    }
}
