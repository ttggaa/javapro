package com.kariqu.zwsrv.web.persistance.service;

import com.kariqu.zwsrv.web.persistance.entityex.WebOp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class OpService {
    @PersistenceContext
    protected EntityManager entityManager;

    public long count() {
        String sql = "select count(1) from web_oplog;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);

        BigInteger v = (BigInteger) query.getResultList().get(0);
        return v.longValue();
    }

    public List<WebOp> limit(int start, int count) {
        String sql = "select * from web_oplog" +
                " order by id desc limit ?, ?;";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, start);
        query.setParameter(2, count);
        return WebOp.parseRowList(query.getResultList());
    }

    @Transactional
    public boolean add(String opkey, String opvalue) {
        String sql = "insert into web_oplog(`opkey`,`opvalue`,`optime`)" +
                "value(?,?,now());";
        javax.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, opkey);
        query.setParameter(2, opvalue);

        int ret = query.executeUpdate();
        if (ret > 0)
            return true;
        return false;
    }
}
