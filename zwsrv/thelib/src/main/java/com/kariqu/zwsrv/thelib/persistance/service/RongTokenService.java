package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RongTokenDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.RongToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 08/12/17.
 */
@Service
public class RongTokenService extends BaseService<RongToken> {

    @Autowired
    private RongTokenDAO rongTokenDAO;

    @Override
    protected JpaRepository<RongToken, Integer> getDao() {
        return rongTokenDAO;
    }
    
}

