package com.kariqu.zwsrv.thelib.persistance.service;


import com.kariqu.zwsrv.thelib.persistance.dao.MergeCostFragmentLogDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.MergeCostFragmentLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MergeCostFragmentLogService extends BaseService<MergeCostFragmentLog> {

    @Autowired
    protected MergeCostFragmentLogDAO mergeCostFragmentLogDAO;

    @Override
    protected JpaRepository<MergeCostFragmentLog, Integer> getDao() { return mergeCostFragmentLogDAO; }
}
