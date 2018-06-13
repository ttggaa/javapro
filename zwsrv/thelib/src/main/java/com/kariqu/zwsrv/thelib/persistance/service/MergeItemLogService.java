package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.MergeItemLogDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.MergeItemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MergeItemLogService extends BaseService<MergeItemLog> {

    @Autowired
    protected MergeItemLogDAO mergeItemLogDAO;

    @Override
    protected JpaRepository<MergeItemLog, Integer> getDao() { return mergeItemLogDAO; }

}
