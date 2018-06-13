package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.PlatDictDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.PlatDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by simon on 21/12/17.
 */
@Service
public class PlatDictService extends BaseService<PlatDict> {

    @Autowired
    private PlatDictDAO platDictDAO;

    @Override
    protected JpaRepository<PlatDict, Integer> getDao() {
        return platDictDAO;
    }

    public List<PlatDict> findByName(List<String> names) {
        return platDictDAO.findByName(names.toArray(new String[names.size()]));
    }

    public List<PlatDict> findAllPlatConfig() {
        return platDictDAO.findAllPlatConfig();
    }

}
