package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.ConfigDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 26/11/17.
 */
@Service
public class ConfigService extends BaseService<Config> {

    @Autowired
    private ConfigDAO configDAO;

    @Override
    protected JpaRepository<Config, Integer> getDao() {
        return configDAO;
    }


    public List<Config> findAllValidByModule(String module) {
        return configDAO.findAllValidByModule(module);
    }

    public List<Config> findAllByModule(String module) {
        return configDAO.findAllByModule(module);
    }

}
