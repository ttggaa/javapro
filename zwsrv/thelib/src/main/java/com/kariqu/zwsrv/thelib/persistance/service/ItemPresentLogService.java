package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.ItemPresentLogDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.ItemPresentLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemPresentLogService extends BaseService<ItemPresentLog> {

    @Autowired
    private ItemPresentLogDAO itemPresentLogDAO;

    @Override
    protected JpaRepository<ItemPresentLog, Integer> getDao() {
        return itemPresentLogDAO;
    }
}
