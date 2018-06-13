package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.RaiseProblemDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.RaiseProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RaiseProblemService extends BaseService<RaiseProblem> {

    @Autowired
    protected RaiseProblemDAO raiseProblemDAO;

    @Override
    protected JpaRepository<RaiseProblem, Integer> getDao() {
        return raiseProblemDAO;
    }

}
