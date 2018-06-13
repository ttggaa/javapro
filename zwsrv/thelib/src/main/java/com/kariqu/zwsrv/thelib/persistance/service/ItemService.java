package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.ItemDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDAO itemDAO;

    @Override
    protected JpaRepository<Item, Integer> getDao() {
        return itemDAO;
    }
}
