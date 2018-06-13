package com.kariqu.zwsrv.thelib.persistance.service;

import com.kariqu.zwsrv.thelib.persistance.dao.PayOrderDAO;
import com.kariqu.zwsrv.thelib.persistance.entity.PayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by simon on 26/11/17.
 */
@Service
public class PayOrderService extends BaseService<PayOrder> {

    @Autowired
    private PayOrderDAO payOrderDAO;

    @Override
    protected JpaRepository<PayOrder, Integer> getDao() {
        return payOrderDAO;
    }


    public PayOrder findByPaySN(String paySN) {
        return payOrderDAO.findByPaySN(paySN);
    }

}
